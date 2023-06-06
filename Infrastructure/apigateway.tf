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

resource "aws_api_gateway_integration" "example" {
  rest_api_id             = aws_api_gateway_rest_api.gis-api.id
  resource_id             = aws_api_gateway_resource.gis-gateway.id
  http_method             = aws_api_gateway_method.gis-method.http_method
  type                    = "AWS"
  integration_http_method = "POST"
  uri                     = aws_sns_topic.gis-sns.arn
}

resource "aws_api_gateway_method_response" "gis-response" {
  rest_api_id = aws_api_gateway_rest_api.gis-api.id
  resource_id = aws_api_gateway_resource.gis-gateway.id
  http_method = aws_api_gateway_method.gis-method.http_method
  status_code = "200"
  response_models = {
    "Content-Type" = "application/x-form-urlencoded"
  }
}

resource "aws_api_gateway_integration_response" "example" {
  rest_api_id    = aws_api_gateway_rest_api.gis-api.id
  resource_id    = aws_api_gateway_resource.gis-gateway.id
  http_method    = aws_api_gateway_method.gis-method.http_method
  status_code    = aws_api_gateway_method_response.gis-response.status_code
}

resource "aws_api_gateway_method_settings" "gis-settings" {
  rest_api_id = aws_api_gateway_rest_api.gis-api.id
  stage_name  = "gis-dev"
  method_path = "${aws_api_gateway_resource.gis-gateway.path_part}/${aws_api_gateway_method.gis-method.http_method}"

  settings {
    metrics_enabled = true
    logging_level   = "INFO"
  }
}
