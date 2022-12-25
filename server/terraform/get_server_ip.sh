#!/usr/bin/env bash
set -exuET -o pipefail
shopt -s inherit_errexit

cd "$(dirname "$0")"
terraform state pull | jq -r "[.resources[] | select(.name == \"${1}\") | .instances[].attributes.ipv4_address] | .[0]"
