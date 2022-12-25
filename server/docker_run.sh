#!/usr/bin/env bash
set -exuET -o pipefail
shopt -s inherit_errexit

case $1 in
  build)
    # shellcheck disable=SC2046
    docker build \
      --tag drl-leaderboard-app-pipeline:v1 $(./build_docker_args.sh) \
      -f Dockerfile ..
    ;;
  bash)
    docker run --rm -it -v "$(pwd)"/docker_state:/home/myuser/docker_state \
      -v "$(pwd)"/ansible:/home/myuser/ansible \
      drl-leaderboard-app-pipeline:v1 bash
    ;;
  *)
    docker run --rm -v "$(pwd)"/docker_state:/home/myuser/docker_state \
      drl-leaderboard-app-pipeline:v1 "$1"
    ;;
esac
exit 0

