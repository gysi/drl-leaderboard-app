#!/usr/bin/env bash
set -e

out=""

# read lines from .dockerenv and echos them as --build-arg [line], ignores lines starting with #
# https://stackoverflow.com/questions/12916352/shell-script-read-missing-last-line
# http://mywiki.wooledge.org/BashFAQ/001
while read -r line || [ -n "$line" ]
do
  [[ $line == "" ]] && continue # skip empty lines
  [[ ! $line == \#* ]] && out+="--build-arg $line "
done <.dockerenv

echo $out
