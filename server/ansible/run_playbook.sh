#!/usr/bin/env bash
set -exuET -o pipefail
shopt -s inherit_errexit

set -xe
cd "$(dirname "$0")"

ip=$(../terraform/get_server_ip.sh "$1")
ansible-playbook --syntax-check -i "${ip}," "$2"
ansible-lint
ansible-playbook -i "${ip}," "$2"

