#!/usr/bin/env bash

WORK_DIR="target"
LANGUAGE_PATH="${WORK_DIR}/jre/languages/bf"

mkdir -p "$LANGUAGE_PATH"
cp -v ../language/target/brainfuck-jvm.jar "$LANGUAGE_PATH"

mkdir -p "$LANGUAGE_PATH/launcher"
cp -v ../launcher/target/bf-launcher.jar "$LANGUAGE_PATH/launcher/"

mkdir -p "$LANGUAGE_PATH/bin"
cp -v ./bf ${LANGUAGE_PATH}/bin/

cd ${WORK_DIR} || exit 1
echo `pwd`

echo Adding jre/
jar cfm bf-component.jar jre/

#echo Adding jre/languages/bf
#jar uf ../bf-component.jar jre/languages/bf

echo Adding jre/languages/bf/launcher
jar uf ../bf-component.jar jre/languages/bf/launcher/bf-launcher.jar

cd classes || exit 1
echo `pwd`

echo Adding META-INF/symlinks
jar uf ../bf-component.jar META-INF/symlinks
echo Adding META-INF/permissions
jar uf ../bf-component.jar META-INF/permissions

