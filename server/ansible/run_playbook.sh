#!/usr/bin/env bash
set -exuET -o pipefail
shopt -s inherit_errexit

set -xe
cd "$(dirname "$0")"

ansible-playbook --syntax-check "$2"
ansible-lint
ip=$(../terraform/get_server_ip.sh "$1")
ansible-playbook -i "${ip}," "$2"

