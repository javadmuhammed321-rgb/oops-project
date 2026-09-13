#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
mkdir -p out
echo "Compiling..."
javac -cp "lib/*" -d out $(find src -name "*.java")
echo "Starting server on http://localhost:8080 ..."
java -cp "out:lib/*" com.localservice.Main
