#!/usr/bin/env bash
set -exuET -o pipefail
shopt -s inherit_errexit

if [[ -e ~/docker_state/terraform.tfstate ]]; then
  cp ~/docker_state/terraform.tfstate ~/terraform/terraform.tfstate
fi

mv /home/myuser/backend/app.jar /home/myuser/ansible/app/backend/app.jar
mv /home/myuser/backend/DRLLeaderboardBackend /home/myuser/ansible/app/backend/native/DRLLeaderboardBackend

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
      ./ansible/run_playbook.sh test playbook_update-server.yaml
      shift
      ;;
    deploy-app-testserver)
      ./ansible/run_playbook.sh test playbook_deploy-app.yaml
      shift
      ;;
    update-prodserver)
      ./ansible/run_playbook.sh prod playbook_update-server.yaml
      shift
      ;;
    deploy-app-prodserver)
      ./ansible/run_playbook.sh prod playbook_deploy-app.yaml
      shift
      ;;
    *)
      echo "Unknown Command: $1"
      exit 1
      ;;
  esac
done
