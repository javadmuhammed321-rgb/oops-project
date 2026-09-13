#!/bin/bash
set -e
mkdir -p lib
cd lib
curl -fsSL -o sqlite-jdbc-3.45.1.0.jar https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.45.1.0/sqlite-jdbc-3.45.1.0.jar
curl -fsSL -o slf4j-api-2.0.9.jar https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar
curl -fsSL -o slf4j-nop-2.0.9.jar https://repo1.maven.org/maven2/org/slf4j/slf4j-nop/2.0.9/slf4j-nop-2.0.9.jar
echo "Libraries downloaded into lib/"
