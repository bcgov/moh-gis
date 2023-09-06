resource "aws_secretsmanager_secret" "gis_jdbc_setting" {
  name = "${var.application}_jdbc_setting"
}

resource "aws_secretsmanager_secret" "gis_proxy_user" {
  name = "${var.application}_user"
}

resource "aws_secretsmanager_secret" "gis_project_stage" {
  name = "${var.application}_project_stage"
}

resource "aws_secretsmanager_secret" "gis_keycloak_client_secret" {
  name = "${var.application}_keycloak_client_secret"
}

resource "aws_secretsmanager_secret" "gis_provider_uri" {
  name = "${var.application}_provider_uri"
}

resource "aws_secretsmanager_secret" "gis_redirect_uri" {
  name = "${var.application}_redirect_uri"
}

resource "aws_secretsmanager_secret" "gis_siteminder_logout_uri" {
  name = "${var.application}_siteminder_logout_uri"
}

resource "aws_secretsmanager_secret" "gis_phsa_logout_uri" {
  name = "${var.application}_phsa_logout_uri"
}

resource "aws_secretsmanager_secret" "gis_aws_api_url" {
  name = "${var.application}_aws_api_url"
}

resource "aws_secretsmanager_secret" "gis_create_immediate_scheduler" {
  name = "${var.application}_create_immediate_scheduler"
}

resource "aws_secretsmanager_secret" "gis_email_subject" {
  name = "${var.application}_email_subject"
}

resource "aws_secretsmanager_secret" "gis_fed_file_host" {
  name = "${var.application}_fed_file_host"
}

resource "aws_secretsmanager_secret" "gis_fed_file_host_user_id" {
  name = "${var.application}_fed_file_host_user_id"
}

resource "aws_secretsmanager_secret" "gis_hars_file_host" {
  name = "${var.application}_hars_file_host"
}

resource "aws_secretsmanager_secret" "gis_hars_file_host_user_id" {
  name = "${var.application}_hars_file_host_user_id"
}

resource "aws_secretsmanager_secret" "gis_hars_file_upload_location" {
  name = "${var.application}_hars_file_upload_location"
}

resource "aws_secretsmanager_secret" "gis_schedule" {
  name = "${var.application}_schedule"
}

resource "aws_secretsmanager_secret_version" "gis_jdbc_setting" {
  secret_id     = aws_secretsmanager_secret.gis_jdbc_setting.id
  secret_string = "changeme"
}

resource "aws_secretsmanager_secret_version" "gis_project_stage" {
  secret_id     = aws_secretsmanager_secret.gis_project_stage.id
  secret_string = "changeme"
}

resource "aws_secretsmanager_secret_version" "gis_keycloak_client_secret" {
  secret_id     = aws_secretsmanager_secret.gis_keycloak_client_secret.id
  secret_string = "changeme"
}

resource "aws_secretsmanager_secret_version" "gis_provider_uri" {
  secret_id     = aws_secretsmanager_secret.gis_provider_uri.id
  secret_string = "changeme"
}

resource "aws_secretsmanager_secret_version" "gis_redirect_uri" {
  secret_id     = aws_secretsmanager_secret.gis_redirect_uri.id
  secret_string = "changeme"
}

resource "aws_secretsmanager_secret_version" "gis_siteminder_logout_uri" {
  secret_id     = aws_secretsmanager_secret.gis_siteminder_logout_uri.id
  secret_string = "changeme"
}

resource "aws_secretsmanager_secret_version" "gis_phsa_logout_uri" {
  secret_id     = aws_secretsmanager_secret.gis_phsa_logout_uri.id
  secret_string = "changeme"
}

resource "aws_secretsmanager_secret_version" "gis_aws_api_url" {
  secret_id     = aws_secretsmanager_secret.gis_aws_api_url.id
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

resource "aws_secretsmanager_secret_version" "gis_fed_file_host" {
  secret_id     = aws_secretsmanager_secret.gis_fed_file_host.id
  secret_string = "changeme"
}

resource "aws_secretsmanager_secret_version" "gis_fed_file_host_user_id" {
  secret_id     = aws_secretsmanager_secret.gis_fed_file_host_user_id.id
  secret_string = "changeme"
}

resource "aws_secretsmanager_secret_version" "gis_hars_file_host" {
  secret_id     = aws_secretsmanager_secret.gis_hars_file_host.id
  secret_string = "changeme"
}

resource "aws_secretsmanager_secret_version" "gis_hars_file_host_user_id" {
  secret_id     = aws_secretsmanager_secret.gis_hars_file_host_user_id.id
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
    ignore_changes = [secret_string]
  }
}
