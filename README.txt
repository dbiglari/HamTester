This is a program that will read in questions from the test pool for the Technician, General, and Extra Classes for the HAM radio exam, and ask random questions from the pool, while keeping score of the number answered correctly.  It asks 35 questions, and displays passing as long as at least 75% of the questions are answered correctly.
This program was written using the Java 1.4 standard, to allow for compiling to a binary with gcj 4.3.


BUILDING

Note: Building a binary requires gcj and/or ant.  Prebuilt binaries are located in the dist folder.


Building a jar using the build.xml file requires ant. To build a jar using the build.xml file, from the command line run:

ant

To build a binary in Windows, from the command line run:

makeBinary.bat

This will produce a Windows binary called "HamTester.exe" in the dist folder. To use the Windows binary, from the command line run:

run.bat

To build a binary in Linux, from the command line run:

./makeBinary.sh

This will produce a Linux binary called "HamTester" in the dist folder.  To use the Linux binary, from the command line run:

./run.sh



RUNNING

To run the jar in Windows, from the command line run:

runJar.bat

To run the jar in Lindows, from the command line run:

./runJar.sh





