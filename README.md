# Brainfuck-JVM
This project provides a Brainfuck implementation on the JVM.
It leverages GraalVM and Truffle.

## About [Brainfuck](https://en.wikipedia.org/wiki/Brainfuck)
Brainfuck is a Turing-complete language with a rather bizarre syntax.
It consists of only eight simple commands, and one instruction pointer.
Its main purpose is to challenge and amuse programmers.

## About [GraalVM](https://www.graalvm.org/)
From the [website](https://www.graalvm.org/):

> GraalVM is a universal virtual machine for running applications written in JavaScript, Python, Ruby, R, JVM-based languages like Java, Scala, Kotlin, Clojure, and LLVM-based languages such as C and C++. 
>
> GraalVM removes the isolation between programming languages and enables interoperability in a shared runtime. It can run either standalone or in the context of OpenJDK, Node.js, Oracle Database, or MySQL. 

## About [Truffle](https://github.com/oracle/graal/tree/master/truffle)
From the [website]():

> Truffle is an Open Source library for building programming language implementations as interpreters for self-modifying Abstract Syntax Trees. Together with the Open Source Graal compiler, Truffle represents a significant step forward in programming language implementation technology in the current era of dynamic languages.

Although a Brainfuck program isn't very likely to modify its Abstract Syntax Tree, it seems that there is currently no other way to make language run inside GraalVM.

## 
[![CircleCI](https://circleci.com/gh/mthmulders/brainfuck-jvm.svg?style=svg)](https://circleci.com/gh/mthmulders/brainfuck-jvm)
[![SonarCloud quality gate](https://sonarcloud.io/api/project_badges/measure?project=mthmulders_brainfuck-jvm&metric=alert_status)](https://sonarcloud.io/dashboard?id=mthmulders_brainfuck-jvm)
[![SonarCloud vulnerability count](https://sonarcloud.io/api/project_badges/measure?project=mthmulders_brainfuck-jvm&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=mthmulders_brainfuck-jvm)
[![SonarCloud technical debt](https://sonarcloud.io/api/project_badges/measure?project=mthmulders_brainfuck-jvm&metric=sqale_index)](https://sonarcloud.io/dashboard?id=mthmulders_brainfuck-jvm)

## Getting started

### Install GraalVM
To get started, you need GraalVM.
You can obtain it at [the GraalVM website](https://www.graalvm.org/downloads/).
Note that currently there are only distributions for Linux and MacOS.

### Build Brainfuck-JVM

    # Or wherever GraalVM is installed...
    # this is a typical MacOS installation path.
    export JAVA_HOME="/Library/Java/JavaVirtualMachines/graalvm-ce-1.0.0-rc9/Contents/Home"
    mvn verify
    
## Using Brainfuck-JVM
The integration test suite (in `SamplesIT.java`) gives a good idea of how to run Brainfuck programs inside the JVM.

The general pattern is as follows:

    final Source source = Source.newBuilder(BrainfuckLanguage.ID, input, null)
        .cached(false)
        .buildLiteral();
    
    try (final Engine engine = Engine.newBuilder().build();
         final Context context = Context.newBuilder().engine(engine).build()) {
        context.eval(source);
    }

## Debugging the generated AST
It can be hard to verify whether the AST that was generated is correct.
To visualise the AST, run your program with `-Dbrainfuck.ast.dump=true`.
It will generate a file called `output.dot`.
You can convert that to an PNG-image using Graphviz: `dot -Tpng output.dot -ooutput.png`.

## Project structure
This project has a few modules:
* **language** - this module contains the actual language implementation.
* **launcher** - this module contains a simple command line Brainfuck launcher.
* **native** - this module builds a native executable for the _launcher_.

## References
The following resources have been very useful while developing this project.

* [GraalVM as a Platform - Implement Your Language](https://www.graalvm.org/docs/graalvm-as-a-platform/implement-language/)
* [One VM to Rule Them All](https://lafo.ssw.uni-linz.ac.at/pub/papers/2016_PLDI_Truffle.pdf)

## License
This project is licensed under the MIT license.
See `./LICENSE` for details.