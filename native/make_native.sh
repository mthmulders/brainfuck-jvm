#!/usr/bin/env bash

[[ -d target ]] || mkdir target

#
# Setting -Dcom.oracle.graalvm.isaot=false is not in the Simple Language example.
# By default, it is set to true. But in CircleCI builds, that run on Linux, this setting combined with the fact that
# Linux has procfs seems to make native-image crash unpredictably.
#

$JAVA_HOME/bin/native-image \
    --verbose \
    --no-server \
    -Dcom.oracle.graalvm.isaot=false \
    --tool:truffle \
    -H:MaxRuntimeCompileMethods=1200 \
    -H:+ReportUnsupportedElementsAtRuntime \
    -cp ../language/target/brainfuck-jvm.jar:../launcher/target/launcher.jar \
    it.mulders.brainfuck.launcher.Launcher \
    target/bfnative
