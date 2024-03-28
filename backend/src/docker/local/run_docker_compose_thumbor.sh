#!/bin/bash

# Define the base directory relative to the script location or an absolute path
BASE_DIR="../../../imgcache"

# List of directories to create under the base directory
DIRS=(
    ""  # The base dir itself
    "result-storage"
    "source-storage"
)

# Loop through the directory names and create them with the correct permissions
for dir in "${DIRS[@]}"; do
    # Construct the full path to the directory
    FULL_DIR_PATH="${BASE_DIR}/${dir}"

    # Check if the directory already exists; create it if it doesn't
    if [ ! -d "${FULL_DIR_PATH}" ]; then
        echo "Creating directory: ${FULL_DIR_PATH}"
        mkdir -p "${FULL_DIR_PATH}"
    else
        echo "Directory already exists: ${FULL_DIR_PATH}"
    fi

    # Set the directory permissions to 0777
    chmod 0777 "${FULL_DIR_PATH}"
done

echo "All directories have been created and permissions set."

docker compose -f docker-compose-thumbor.yaml up
