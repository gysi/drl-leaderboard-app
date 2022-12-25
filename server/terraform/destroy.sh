#!/usr/bin/env bash
set -exuET -o pipefail
shopt -s inherit_errexit

cd "$(dirname "$0")"
cp ../docker_state/terraform.tfstate terraform.tfstate
terraform destroy -var-file=config.tfvars -auto-approve
if [ $? -eq 0 ]
then
  rm ../docker_state/terraform.tfstate
fi
