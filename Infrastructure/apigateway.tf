resource "aws_api_gateway_rest_api" "gis-api" {
  name        = "gis-dev-sns"
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
  uri = "arn:aws:apigateway:ca-central-1:sns:action/Publish"
  credentials = aws_iam_role.api_execution_role.arn
}

resource "aws_api_gateway_method_response" "gis-response" {
  rest_api_id = aws_api_gateway_rest_api.gis-api.id
  resource_id = aws_api_gateway_resource.gis-gateway.id
  http_method = aws_api_gateway_method.gis-method.http_method
  status_code = "200"
}

resource "aws_api_gateway_integration_response" "gis-int-reponse" {
  rest_api_id    = aws_api_gateway_rest_api.gis-api.id
  resource_id    = aws_api_gateway_resource.gis-gateway.id
  http_method    = aws_api_gateway_method.gis-method.http_method
  status_code    = aws_api_gateway_method_response.gis-response.status_code

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
