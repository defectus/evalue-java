Payment Tracker
===============

Installation
------------

Prerequisites

Java 8 has to be installed on the target machine.   
Git must be installed on the target machine in order to access the source code. 

Gradle

This application is built by Gradle. You don't have to have Gradle installed on your PC but building and running the application using a system-wide installation can speed things up.

To build the application navigate to the folder you cloned the source code into and run `./gradlew jar` (or `gradlew jar`on Windows). If you have Gradle already installed just run `gradle jar`.

Running
-------

To run the application just issue this command `java -jar build/libs/evalue-java-1.0.jar`. As per the requirements you can optionally provide an input file. To do so just run the application with the input file's name as an argument e.g. `java -jar build/libs/evalue-java-1.0.jar input.txt`.
