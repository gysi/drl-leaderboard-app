#!/usr/bin/env bash
set -exuET -o pipefail
shopt -s inherit_errexit

cd "$(dirname "$0")"
terraform apply \
-var-file=config.tfvars \
-auto-approve

if [ $? -eq 0 ]
then
  cp terraform.tfstate ../docker_state/terraform.tfstate
fi
