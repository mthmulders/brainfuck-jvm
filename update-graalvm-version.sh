#!/usr/bin/env bash
set -euo pipefail

newversion=$1

echo Patching component/src/main/scripts/bf
sed -i '' -e \
  's/^VERSION=.*$/VERSION=\"'$newversion'\"/g' \
  component/src/main/scripts/bf

echo Patching component/src/main/resources/META-INF/MANIFEST.MF
sed -i '' -e \
  's/graalvm_version=.*)(/graalvm_version='$newversion')(/g' \
  component/src/main/resources/META-INF/MANIFEST.MF

echo Patching .circleci/config.yml
sed -i '' -e \
  's/graalvm-ce:.*-java/graalvm-ce:'$newversion'-java/g' \
  .circleci/config.yml
