#!/bin/bash

output_file="combined_project.txt"

# Create or clear the output file
> "$output_file"

# Function to check if a file is ignored by git
is_git_ignored() {
    git check-ignore -q "$1"
    return $? # Return the exit status directly
}

# Traverse the project directory
find . -type f | while read -r file; do
    # Normalize the file path by removing the leading './'
    normalized_file=${file#./}

    # Skip if the file matches specific patterns
    if [[ $normalized_file == *".gitignore" ]] ||
       [[ $normalized_file == *".cmd" ]] ||
       [[ $normalized_file == *"META-INF/"* ]] ||
       [[ $normalized_file == *".git/"* ]] ||
       [[ $normalized_file == *".yarn/"* ]] ||
       [[ $normalized_file == *".pnp."* ]] ||
       [[ $normalized_file == *"mvnw"* ]] ||
       [[ $normalized_file == *".jar" ]] ||
       [[ $normalized_file == *"LICENSE.txt" ]] ||
       [[ $normalized_file == *".lock" ]] ||
       [[ $normalized_file == *".ico" ]] ||
       [[ $normalized_file == *".zip" ]] ||
       [[ $normalized_file == *"create_file_for_custom_gpt.sh" ]] ||
       [[ $normalized_file == *".dockerenv" ]] ||
       [[ $normalized_file == *".ssh"* ]] ||
       [[ $normalized_file == *"docker_compose.env" ]] ||
       [[ $normalized_file == *"application.yaml" ]] ||
       [[ $normalized_file == *"application-local.yaml" ]] ||
       [[ $normalized_file =~ \.(jpg|jpeg|png|gif|bmp|svg|webp|obj)$ ]]; then
        continue
    fi

    if is_git_ignored "$normalized_file"; then
        continue
    fi

    echo "Start of File: $normalized_file" >> "$output_file"
    cat "$normalized_file" >> "$output_file"
    echo -e "\nEnd of File: $normalized_file\n" >> "$output_file"
done

echo "Combination complete. Output in $output_file"
