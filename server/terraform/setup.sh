#!/usr/bin/env bash
set -e
terraform apply \
-var-file=config.tfvars \
-auto-approve

if [ $? -eq 0 ]
then
  cp terraform.tfstate ../docker_state/terraform.tfstate
fi
