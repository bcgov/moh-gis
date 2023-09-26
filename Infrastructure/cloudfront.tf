# locals {
#   alb_origin_id = "fmdb.ynr9ed-dev.nimbus.cloud.gov.bc.ca"
# }

# provider "aws" {
#   alias = "us-east-1"
#   region = "us-east-1"
# }

# data "aws_acm_certificate" "fmdb_certificate" {
#   provider = aws.us-east-1
#   domain = "fmdbd.hlth.gov.bc.ca"
#   statuses = ["ISSUED"]
#   most_recent = true
# }

# data "aws_cloudfront_cache_policy" "CachingDisabled" {
#   name = "Managed-CachingDisabled"
# }

# data "aws_cloudfront_origin_request_policy" "AllViewerExceptHostHeader"{
#     name = "Managed-AllViewerExceptHostHeader"
# }

# resource "aws_cloudfront_distribution" "fmdb_distribution" {
#   origin {
#     domain_name = local.alb_origin_id
#     origin_id   = local.alb_origin_id
#     custom_origin_config {
#     http_port = 80
#     https_port = 443
#     origin_ssl_protocols = ["TLSv1.2"]
#     origin_protocol_policy = "https-only"
#     }
#     origin_shield {
#       enabled = true
#       origin_shield_region = "us-east-1"
#     }
#   }

#   enabled             = true
#   is_ipv6_enabled     = true
#   aliases = ["fmdbd.hlth.gov.bc.ca"]
#   comment             = "fmdb-cloudfront"

#   # Configure logging here if required 	
#   #logging_config {
#   #  include_cookies = false
#   #  bucket          = "mylogs.s3.amazonaws.com"
#   #  prefix          = "myprefix"
#   #}

#   # If you have domain configured use it here 
#   #aliases = ["mywebsite.example.com", "s3-static-web-dev.example.com"]

#   default_cache_behavior {
#     allowed_methods  = ["GET", "HEAD","OPTIONS","PUT","POST","PATCH","DELETE"]
#     cached_methods   = ["GET", "HEAD"]
#     target_origin_id = local.alb_origin_id
#     cache_policy_id = data.aws_cloudfront_cache_policy.CachingDisabled.id
#     origin_request_policy_id = data.aws_cloudfront_origin_request_policy.AllViewerExceptHostHeader.id
#     compress = true
#     viewer_protocol_policy = "redirect-to-https"
#   }

#   price_class = "PriceClass_100"

#   restrictions {
#     geo_restriction {
#       restriction_type = "whitelist"
#       locations        = ["CA"]
#     }
#   }
#   lifecycle {
#     prevent_destroy = true
#   }

# #   tags = {
# #     Environment = "development"
# #     Name        = "my-tag"
# #   }

#   viewer_certificate {
#     acm_certificate_arn = data.aws_acm_certificate.fmdb_certificate.arn
#     minimum_protocol_version = "TLSv1.2_2021"
#     ssl_support_method = "sni-only"
#   }
# }

# # to get the Cloud front URL if doamin/alias is not configured
# output "cloudfront_domain_name" {
#   value = aws_cloudfront_distribution.fmdb_distribution.domain_name
# }
