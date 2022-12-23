#!/bin/bash
set -e

while [[ $# -gt 0 ]]
do
  case $1 in
    create-server)
      cd /terraform && ./setup.sh
      shift
      ;;
    destroy-server)
      cd /terraform && ./destroy.sh
      shift
      ;;
    deploy-app)
      bash
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
