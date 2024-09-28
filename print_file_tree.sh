#!/bin/bash

tree_func_backend () {
  tree -n --noreport -I 'imgcache|target|logs|test|protoc|data|native-image' backend
}

tree_func_frontend () {
  tree -n --noreport -I '.quasar|.vscode|.yarn|dist|node_modules|icons|maps|drone_parts|gates' frontend
}

filtering_func () {
  awk '{ gsub(/( | )( | )( | )( | )/, "!!!"); print }' | awk '{ gsub(/( | )( | )( | )/, "!!"); print }' | awk '{ gsub(/──/, "─"); print }' | awk '{ gsub(/!/, " "); print }'
}

(
tree_func_backend | filtering_func
)

(
tree_func_frontend | filtering_func
)
