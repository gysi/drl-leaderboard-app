#!/bin/bash
set -xe

if [[ -e ~/docker_state/terraform.tfstate ]]; then
  cp ~/docker_state/terraform.tfstate ~/terraform/terraform.tfstate
fi

while [[ $# -gt 0 ]]
do
  case $1 in
    create-server)
      cd terraform && ./setup.sh
      shift
      ;;
    destroy-server)
      cd terraform && ./destroy.sh
      shift
      ;;
    deploy-app)
      ansible-playbook -i $(terraform/get_server_ip.sh), ansible/playbook.yaml
      shift
      ;;
    bash)
      bash
      shift
      ;;
    *)
      echo "Unknown Command: $1"
      exit 0
      ;;
  esac
done
