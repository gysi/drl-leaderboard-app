#!/usr/bin/env bash
set -exuET -o pipefail
shopt -s inherit_errexit

set -xe
cd "$(dirname "$0")"
cp -r env/"$2"/* app/
ip=$(../terraform/get_server_ip.sh "$1")
ansible-playbook --syntax-check -i "${ip}," -e "@vars/${2}_server.yaml" "$3"
ansible-lint
ansible-playbook -v -i "${ip}," -e "@vars/${2}_server.yaml" "$3"

