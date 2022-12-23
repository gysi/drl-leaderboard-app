#!/usr/bin/env bash
set -e
cp ../docker_state/terraform.tfstate terraform.tfstate
terraform destroy -var-file=config.tfvars -auto-approve
if [ $? -eq 0 ]
then
  rm ../docker_state/terraform.tfstate
fi
