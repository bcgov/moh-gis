resource "aws_secretsmanager_secret" "gis_jdbc_setting" {
  name = "${var.application}_jdbc_setting"
}

resource "aws_secretsmanager_secret" "gis_keycloak_client_secret" {
  name = "${var.application}_keycloak_client_secret"
}

resource "aws_secretsmanager_secret" "gis_proxy_user" {
  name = "${var.application}_user"
}

resource "aws_secretsmanager_secret_version" "gis_jdbc_setting" {
  secret_id     = aws_secretsmanager_secret.gis_jdbc_setting.id
  secret_string = "changeme"
}

resource "aws_secretsmanager_secret_version" "gis_keycloak_client_secret" {
  secret_id     = aws_secretsmanager_secret.gis_keycloak_client_secret.id
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
