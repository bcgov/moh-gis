resource "aws_api_gateway_rest_api" "gis-api" {
  name = "gis-dev-sns"
}

resource "aws_api_gateway_resource" "gis-gateway" {
  rest_api_id = aws_api_gateway_rest_api.gis-api.id
  parent_id   = aws_api_gateway_rest_api.gis-api.root_resource_id
  path_part   = "example"
}

resource "aws_api_gateway_method" "gis-method" {
  rest_api_id   = aws_api_gateway_rest_api.gis-api.id
  resource_id   = aws_api_gateway_resource.gis-gateway.id
  http_method   = "POST"
  authorization = "NONE"
}

resource "aws_api_gateway_integration" "gis-integration" {
  rest_api_id             = aws_api_gateway_rest_api.gis-api.id
  resource_id             = aws_api_gateway_resource.gis-gateway.id
  http_method             = aws_api_gateway_method.gis-method.http_method
  type                    = "AWS"
  integration_http_method = "POST"
  uri                     = "arn:aws:apigateway:ca-central-1:sns:action/Publish"
  credentials             = aws_iam_role.api_execution_role.arn
}

resource "aws_api_gateway_method_response" "gis-response" {
  rest_api_id = aws_api_gateway_rest_api.gis-api.id
  resource_id = aws_api_gateway_resource.gis-gateway.id
  http_method = aws_api_gateway_method.gis-method.http_method
  status_code = "200"
}

resource "aws_api_gateway_integration_response" "gis-int-reponse" {
  rest_api_id = aws_api_gateway_rest_api.gis-api.id
  resource_id = aws_api_gateway_resource.gis-gateway.id
  http_method = aws_api_gateway_method.gis-method.http_method
  status_code = aws_api_gateway_method_response.gis-response.status_code
  depends_on = [
    aws_api_gateway_integration.gis-integration
  ]
}
resource "aws_api_gateway_deployment" "gis_deploy" {
  rest_api_id = aws_api_gateway_rest_api.gis-api.id
}

resource "aws_api_gateway_stage" "gis-stage" {
  deployment_id = aws_api_gateway_deployment.gis_deploy.id
  rest_api_id   = aws_api_gateway_rest_api.gis-api.id
  stage_name    = "gis-stage"
}

resource "aws_api_gateway_method_settings" "gis-settings" {
  rest_api_id = aws_api_gateway_rest_api.gis-api.id
  stage_name  = aws_api_gateway_stage.gis-stage.stage_name
  method_path = "${aws_api_gateway_resource.gis-gateway.path_part}/${aws_api_gateway_method.gis-method.http_method}"

  settings {
    #metrics_enabled = false
    #Slogging_level   = "INFO"
  }
}

data "aws_acm_certificate" "gis_certificate" {
  domain      = "gis${substr(var.target_env,0,1)}.hlth.gov.bc.ca"
  statuses    = ["ISSUED"]
  most_recent = true
}

module "api_gateway" {
  source  = "terraform-aws-modules/apigateway-v2/aws"
  version = "2.2.2"

  name                   = "${var.application}-http-api"
  description            = "HTTP API Gateway"
  protocol_type          = "HTTP"
  create_api_domain_name = false

  domain_name                              = "gis${substr(var.target_env,0,1)}.hlth.gov.bc.ca"
  domain_name_certificate_arn              = data.aws_acm_certificate.gis_certificate.arn

  integrations = {
    "ANY /{proxy+}" = {
      connection_type    = "VPC_LINK"
      vpc_link           = "gis-vpc"
      integration_uri    = data.aws_alb_listener.front_end.arn
      integration_type   = "HTTP_PROXY"
      integration_method = "ANY"
    }
  }
  vpc_links = {
    gis-vpc = {
      name               = "${var.application}-vpc-link"
      security_group_ids = [data.aws_security_group.web.id]
      subnet_ids         = data.aws_subnets.web.ids
    }
  }

  #   tags = {
  #     Name = "test-api-new"
  #   }
}
