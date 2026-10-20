provider "aws" {
  alias = "us-east-1"
  region = "us-east-1"
}

data "aws_acm_certificate" "gis_cloudfront_certificate" {
  provider    = aws.us-east-1
  domain      = var.application_url
  statuses    = ["ISSUED"]
  most_recent = true
}
locals {
  api_gateway_domain = replace(module.api_gateway.apigatewayv2_api_api_endpoint, "https://", "")
}

data "aws_cloudfront_cache_policy" "CachingDisabled" {
  name = "Managed-CachingDisabled"
}

data "aws_cloudfront_origin_request_policy" "AllViewerExceptHostHeader"{
    name = "Managed-AllViewerExceptHostHeader"
}

resource "aws_cloudfront_response_headers_policy" "security_headers_policy" {
  name    = "${var.application}-security-headers"
  comment = "STS, CSP and Permissions-Policy"

  security_headers_config {
    strict_transport_security {
      access_control_max_age_sec = 31536000
      include_subdomains         = true
      preload                    = false
      override                   = true
    }

    content_security_policy {
      content_security_policy = "default-src 'self';  script-src 'self';  style-src 'self';  img-src 'self' data:;  font-src 'self';  object-src 'none';  frame-ancestors 'none';  base-uri 'self';  form-action 'self'; upgrade-insecure-requests;"
      override                = false
    }
  }

  custom_headers_config {
    items {
      header   = "Permissions-Policy"
      value    = "camera=(), microphone=(), geolocation=(), payment=(), usb=(), fullscreen=(self)"
      override = true
    }
  }
}

resource "aws_cloudfront_distribution" "gis_distribution" {
  origin {
    domain_name = local.api_gateway_domain
    origin_id   = local.api_gateway_domain
    custom_origin_config {
    http_port = 80
    https_port = 443
    origin_ssl_protocols = ["TLSv1.2"]
    origin_protocol_policy = "https-only"
    }
    origin_shield {
      enabled = true
      origin_shield_region = "us-east-1"
    }
  }
  aliases = [var.application_url]
  
  enabled             = true
  is_ipv6_enabled     = true
  comment             = "gis-cloudfront"

#   # Configure logging here if required 	
#   #logging_config {
#   #  include_cookies = false
#   #  bucket          = "mylogs.s3.amazonaws.com"
#   #  prefix          = "myprefix"
#   #}

  default_cache_behavior {
    allowed_methods  = ["GET", "HEAD","OPTIONS","PUT","POST","PATCH","DELETE"]
    cached_methods   = ["GET", "HEAD"]
    target_origin_id = local.api_gateway_domain
    cache_policy_id = data.aws_cloudfront_cache_policy.CachingDisabled.id
    origin_request_policy_id = data.aws_cloudfront_origin_request_policy.AllViewerExceptHostHeader.id
    compress = true
    viewer_protocol_policy = "redirect-to-https"
    response_headers_policy_id = aws_cloudfront_response_headers_policy.security_headers_policy.id
  }

   price_class = "PriceClass_100"

   restrictions {
     geo_restriction {
       restriction_type = "whitelist"
       locations        = ["CA"]
     }
   }
   lifecycle {
     prevent_destroy = true
   }

# #   tags = {
# #     Environment = "development"
# #     Name        = "my-tag"
# #   }

   viewer_certificate {
     acm_certificate_arn = data.aws_acm_certificate.gis_cloudfront_certificate.arn
     minimum_protocol_version = "TLSv1.2_2021"
     ssl_support_method = "sni-only"
   }
 }

# # to get the Cloud front URL if doamin/alias is not configured
 output "cloudfront_domain_name" {
   value = aws_cloudfront_distribution.gis_distribution.domain_name
 }
