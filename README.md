# Groovy Libraries for IKAN ALM phases

## General Info

## Technologies

## Setup
The project depends on some external program and tools for running and testint.
They are not libraries dependencies so you should configure the environment.

Assumptions:

A tool directory, default ../lib (where current directory is where main gradle resides), where you should have:
- Slik-Svn, it comes with an installer but you could grab an existing installation, you may simply copy the bin folder
- Groovy 4.0.6, unzip under tool directory and then set GROOVY_HOME
- Gradle 7.6, unzip under tool directory and then set GRADLE_HOME 

A local maven repo, default is ../lib (where current directory is where main gradle resides)

Windows batch file: you may adjust configuration in setenv.bat

## Status