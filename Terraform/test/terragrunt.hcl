include {
  path = find_in_parent_folders()
}

locals {
  project = get_env("LICENSE_PLATE")
#   commontags = [
#     environment = "dev",
#     application = "fmdb"
#   ]
}

generate "test_tfvars" {
  path              = "test.auto.tfvars"
  if_exists         = "overwrite"
  disable_signature = true
  contents          = <<-EOF
  fargate_cpu = 512
  fargate_memory = 1024
  app_port = 8181
  fam_console_idp_name = "TEST-IDIR"
  EOF
}