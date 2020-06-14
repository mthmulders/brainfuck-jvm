#!/usr/bin/env bash
set -euo pipefail

gu install -L component/target/brainfuck-component.jar
output=$(bf language/src/test/resources/hello.bf 2>&1 || true)

if [ "$output" != "Hello World!" ]
then
  echo "Expected output of Hello World program to be \"Hello World!\" but it was \"$output\""
  exit 1
fi

output=$(bf -Dbf.mem.size=1 language/src/test/resources/hello.bf 2>&1 || true)
if [ "$output" != "Invalid memory location (1) accessed at line 1, position 13 (+)." ]
then
  echo "Expected output of Hello World program with too few memory to be \"Invalid memory location (1) accessed at line 1, position 13 (+).\" but it was \"$output\""
  exit 1
fi

echo "All tests ran successfully!"