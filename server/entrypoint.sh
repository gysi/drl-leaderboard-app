#!/usr/bin/env bash
set -exuET -o pipefail
shopt -s inherit_errexit

if [[ -e ~/docker_state/terraform.tfstate ]]; then
  cp ~/docker_state/terraform.tfstate ~/terraform/terraform.tfstate
fi

while [[ $# -gt 0 ]]
do
  case $1 in
    bash)
      bash
      shift
      ;;
    create-server)
      ./terraform/setup.sh
      shift
      ;;
    destroy-server)
      ./terraform/destroy.sh
      shift
      ;;
    update-testserver)
      ./ansible/run_playbook.sh test test playbook_update-server.yaml
      shift
      ;;
    deploy-app-testserver)
      ./ansible/run_playbook.sh test test playbook_deploy-app.yaml
      shift
      ;;
    deploy-app-testserver-quick)
      ./ansible/run_playbook.sh test test playbook_deploy-app-quick.yaml
      shift
      ;;
    update-prodserver)
      ./ansible/run_playbook.sh prod2 prod playbook_update-server.yaml
      shift
      ;;
    deploy-app-prodserver)
      ./ansible/run_playbook.sh prod2 prod playbook_deploy-app.yaml
      shift
      ;;
    deploy-app-prodserver-quick)
      ./ansible/run_playbook.sh prod2 prod playbook_deploy-app-quick.yaml
      shift
      ;;
    *)
      echo "Unknown Command: $1"
      exit 1
      ;;
  esac
done
