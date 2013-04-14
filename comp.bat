@echo off

rem Batch file to build JUkagaka
rem build 2013-4-14

set CP="./bin;./lib/jdom-2.0.4.jar;./lib/jaxen-1.1.4.jar;./lib/DJNativeSwing.jar;./lib/DJNativeSwing-SWT.jar"

javac -d "./bin" -cp %CP% -Xlint:unchecked -Xlint:deprecation -g "%1"