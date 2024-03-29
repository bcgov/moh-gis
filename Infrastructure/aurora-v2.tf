resource "random_pet" "gis_subnet_group_name" {
  prefix = "${var.application}-subnet-group"
  length = 2
}

resource "random_password" "gis_master_password" {
  length           = 16
  special          = true
  override_special = "!#$%&*()-_=+[]{}<>:?"
}

variable "gis_master_username" {
  description = "The username for the DB master user"
  type        = string
  default     = "postgres"
  sensitive   = true
}

variable "gis_database_name" {
  description = "The name of the database"
  type        = string
  default     = "gis"
}

resource "aws_db_subnet_group" "gis_subnet_group" {
  description = "For Aurora cluster ${var.gis_cluster_name}"
  name        = "${var.gis_cluster_name}-subnet-group"
  subnet_ids  = data.aws_subnets.app.ids
  tags = {
    managed-by = "terraform"
  }
  tags_all = {
    managed-by = "terraform"
  }
}

module "aurora_postgresql_v2" {
  source  = "terraform-aws-modules/rds-aurora/aws"
  version = "7.7.1"

  name              = "${var.gis_cluster_name}-${var.target_env}"
  engine            = "aurora-postgresql"
  engine_mode       = "provisioned"
  engine_version    = "14.9"
  storage_encrypted = true
  database_name     = var.gis_database_name

  vpc_id                 = data.aws_vpc.main.id
  vpc_security_group_ids = [data.aws_security_group.data.id]
  db_subnet_group_name   = aws_db_subnet_group.gis_subnet_group.name

  master_username = var.gis_master_username
  master_password = random_password.gis_master_password.result

  create_cluster         = true
  create_security_group  = false
  create_db_subnet_group = false
  create_monitoring_role = false
  create_random_password = false

  apply_immediately   = true
  skip_final_snapshot = true

  db_parameter_group_name         = aws_db_parameter_group.gis_postgresql13.id
  db_cluster_parameter_group_name = aws_rds_cluster_parameter_group.gis_postgresql13.id

  serverlessv2_scaling_configuration = {
    min_capacity = var.aurora_acu_min
    max_capacity = var.aurora_acu_max
  }

  instance_class = "db.serverless"
  instances = {
    one = {}
    two = {}
  }

  tags = {
    managed-by = "terraform"
  }

  enabled_cloudwatch_logs_exports = ["postgresql"]
}

resource "aws_db_parameter_group" "gis_postgresql13" {
  name        = "${var.gis_cluster_name}-parameter-group"
  family      = "aurora-postgresql14"
  description = "${var.gis_cluster_name}-parameter-group"
  tags = {
    managed-by = "terraform"
  }
}

resource "aws_rds_cluster_parameter_group" "gis_postgresql13" {
  name        = "${var.gis_cluster_name}-cluster-parameter-group"
  family      = "aurora-postgresql14"
  description = "${var.gis_cluster_name}-cluster-parameter-group"
  tags = {
    managed-by = "terraform"
  }
  parameter {
    name  = "timezone"
    value = var.timezone
  }
}

resource "random_pet" "master_creds_secret_name" {
  prefix = "gis-master-creds"
  length = 2
}

resource "aws_secretsmanager_secret" "gis_mastercreds_secret" {
  name = random_pet.master_creds_secret_name.id
  tags = {
    managed-by = "terraform"
  }
}

resource "aws_secretsmanager_secret_version" "gis_mastercreds_secret_version" {
  secret_id     = aws_secretsmanager_secret.gis_mastercreds_secret.id
  secret_string = <<EOF
   {
    "username": "${var.gis_master_username}",
    "password": "${random_password.gis_master_password.result}",
    "engine": "14.9",
    "host": "${module.aurora_postgresql_v2.cluster_endpoint}",
    "port": ${module.aurora_postgresql_v2.cluster_port},
    "dbClusterIdentifier": "${module.aurora_postgresql_v2.cluster_id}"
    "dbname": "gis_db"
   }
  EOF
  lifecycle {
    ignore_changes = [secret_string]
  }
}

resource "random_password" "gis_api_password" {
  length           = 16
  special          = true
  override_special = "!#$%&*()-_=+[]{}<>:?"
}

variable "gis_api_username" {
  description = "The username for the DB api user"
  type        = string
  default     = "fam_proxy_api"
  sensitive   = true
}

resource "random_pet" "api_creds_secret_name" {
  prefix = "gis-api-creds"
  length = 2
}

resource "aws_secretsmanager_secret" "gis_apicreds_secret" {
  name = random_pet.api_creds_secret_name.id
  tags = {
    managed-by = "terraform"
  }
}

resource "aws_secretsmanager_secret_version" "gis_apicreds_secret_version" {
  secret_id     = aws_secretsmanager_secret.gis_apicreds_secret.id
  secret_string = <<EOF
   {
    "username": "${var.gis_api_username}",
    "password": "${random_password.gis_api_password.result}",
    "engine": "14.9",
    "host": "${module.aurora_postgresql_v2.cluster_endpoint}",
    "port": ${module.aurora_postgresql_v2.cluster_port},
    "dbClusterIdentifier": "${module.aurora_postgresql_v2.cluster_id}"
   }
  EOF
  lifecycle {
    ignore_changes = [secret_string]
  }
}
