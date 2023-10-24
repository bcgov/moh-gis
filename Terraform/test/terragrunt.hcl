include {
  path = find_in_parent_folders()
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
  alb_origin_id = "gis.ynr9ed-test.nimbus.cloud.gov.bc.ca"
  aurora_acu_min = 0.5
  aurora_acu_max = 4
  EOF
}