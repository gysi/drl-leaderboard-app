#!/usr/bin/env bash
set -exuET -o pipefail
shopt -s inherit_errexit

set -xe
cd "$(dirname "$0")"
cp -r env/"$1"/* app/
ip=$(../terraform/get_server_ip.sh "$1")
ansible-playbook --syntax-check -i "${ip}," -e "@vars/${1}_server.yaml" "$2"
ansible-lint
ansible-playbook -v -i "${ip}," -e "@vars/${1}_server.yaml" "$2"

