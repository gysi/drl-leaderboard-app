#!/usr/bin/env bash
set -e

cd ~/terraform
echo $(terraform state pull | jq -r "[.resources[] | select(.type == \"hcloud_server\") | .instances[].attributes.ipv4_address] | .[0]")
