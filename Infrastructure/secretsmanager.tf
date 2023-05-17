resource "aws_secretsmanager_secret" "jdbc_setting" {
  name = "gis_jdbc_setting"
}

resource "aws_secretsmanager_secret" "fmdb_proxy_user" {
  name = "gis_user"
}


resource "aws_secretsmanager_secret" "fmdb_keycloak-client-secret" {
  name = "gis_keycloak-client-secret"
}

resource "aws_secretsmanager_secret" "redirect_uri"{ 
  name = "redirect_uri"
}

resource "aws_secretsmanager_secret_version" "jdbc_setting" {
  secret_id     = aws_secretsmanager_secret.jdbc_setting.id
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

resource "aws_secretsmanager_secret_version" "redirect_uri" {
  secret_id     = aws_secretsmanager_secret.redirect_uri.id
  secret_string = "changeme"
}
