#!/usr/bin/env bash

[[ -d target ]] || mkdir target

$JAVA_HOME/bin/native-image \
    --verbose \
    --no-server \
    --tool:truffle \
    -H:MaxRuntimeCompileMethods=1200 \
    -H:+ReportUnsupportedElementsAtRuntime \
    -cp ../language/target/brainfuck-jvm.jar:../launcher/target/launcher.jar \
    it.mulders.brainfuck.launcher.Launcher \
    target/bfnative
