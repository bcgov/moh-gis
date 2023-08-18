resource "aws_secretsmanager_secret" "gis_jdbc_setting" {
  name = "${var.application}_jdbc_setting"
}

resource "aws_secretsmanager_secret" "gis_proxy_user" {
  name = "${var.application}_user"
}

resource "aws_secretsmanager_secret" "gis_keycloak-client-secret" {
  name = "${var.application}_keycloak-client-secret"
}

resource "aws_secretsmanager_secret" "gis_redirect_uri"{ 
  name = "${var.application}_redirect_uri"
}

resource "aws_secretsmanager_secret" "gis_provider_uri"{ 
  name = "${var.application}_provider_uri"
}

resource "aws_secretsmanager_secret" "gis_siteminder_uri"{ 
  name = "${var.application}_siteminder_uri"
}

resource "aws_secretsmanager_secret_version" "gis_jdbc_setting" {
  secret_id     = aws_secretsmanager_secret.gis_jdbc_setting.id
  secret_string = "changeme"
}

resource "aws_secretsmanager_secret_version" "gis_api_uri" {
  secret_id     = aws_secretsmanager_secret.gis_api_uri.id
  secret_string = "changeme"
}

resource "aws_secretsmanager_secret_version" "gis_fed_file_host" {
  secret_id     = aws_secretsmanager_secret.gis_fed_file_host.id
  secret_string = "changeme"
}

resource "aws_secretsmanager_secret_version" "gis_hars_file_host" {
  secret_id     = aws_secretsmanager_secret.gis_hars_file_host.id
  secret_string = "changeme"
}

resource "aws_secretsmanager_secret_version" "gis_create_immediate_scheduler" {
  secret_id     = aws_secretsmanager_secret.gis_create_immediate_scheduler.id
  secret_string = "changeme"
}

resource "aws_secretsmanager_secret_version" "gis_email_subject" {
  secret_id     = aws_secretsmanager_secret.gis_email_subject.id
  secret_string = "changeme"
}

resource "aws_secretsmanager_secret_version" "gis_fed_file_host_user_id" {
  secret_id     = aws_secretsmanager_secret.gis_fed_file_host_user_id.id
  secret_string = "changeme"
}

resource "aws_secretsmanager_secret_version" "gis_hars_file_upload_location" {
  secret_id     = aws_secretsmanager_secret.gis_hars_file_upload_location.id
  secret_string = "changeme"
}

resource "aws_secretsmanager_secret_version" "gis_schedule" {
  secret_id     = aws_secretsmanager_secret.gis_schedule.id
  secret_string = "changeme"
}

resource "aws_secretsmanager_secret_version" "rds_credentials" {
  secret_id     = aws_secretsmanager_secret.gis_proxy_user.id
  secret_string = <<EOF
{
  "username": "gis_proxy_user",
  "password": "changeme",
  "engine": "${data.aws_rds_engine_version.postgresql.version}",
  "host": "${module.aurora_postgresql_v2.cluster_endpoint}",
  "port": ${module.aurora_postgresql_v2.cluster_port},
  "dbClusterIdentifier": "${module.aurora_postgresql_v2.cluster_id}"
}
EOF
lifecycle {
  ignore_changes = [ secret_string  ]
  }
}

resource "aws_secretsmanager_secret_version" "gis_keycloak-client-secret" {
  secret_id     = aws_secretsmanager_secret.gis_keycloak-client-secret.id
  secret_string = "changeme"
}

resource "aws_secretsmanager_secret_version" "gis_redirect_uri" {
  secret_id     = aws_secretsmanager_secret.gis_redirect_uri.id
  secret_string = "changeme"
}

resource "aws_secretsmanager_secret_version" "gis_provider_uri" {
  secret_id     = aws_secretsmanager_secret.gis_provider_uri.id
  secret_string = "changeme"
}

resource "aws_secretsmanager_secret_version" "gis_siteminder_uri" {
  secret_id     = aws_secretsmanager_secret.gis_siteminder_uri.id
  secret_string = "changeme"
}
