This is a program that will read in questions from the test pool for the Technician, General, and Extra Classes for the HAM radio exam, and ask random questions from the pool, while keeping score of the number answered correctly.  It asks 35 questions, and displays passing as long as at least 75% of the questions are answered correctly.
This program was written using the Java 1.4 standard, to allow for compiling to a binary with gcj 4.3.


BUILDING

Note: Building the HamTester binary requires gcj (for native binaries) and/or ant (for jar JVM builds).  Prebuilt binaries that have been tested in Windows 10 and Ubuntu 16.04/20.04 are located in the dist folder.  The prebuilt jar in the dist folder was built using Java 1.8, which can be obtained here: https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html


Building the HamTester jar using the build.xml file requires ant. To build a jar using the build.xml file, from the command line run:

rm -f dist/HamTester.jar
ant -f compile_java.xml clean
ant -f compile_java.xml
ant -f pack_jar.xml

To build a binary in Windows, from the command line run:

makeBinary.bat

This will produce a Windows binary called "HamTester.exe" in the dist folder. 

To build a binary in Linux, from the command line run:

./makeBinary.sh

This will produce a Linux binary called "HamTester" in the dist folder.



RUNNING THE BINARY

To use the Windows HamTester binary, from the command line run:

run.bat

To use the Linux HamTester binary, from the command line run:

./run.sh


RUNNING THE JAR

Running the HamTester Jar requires a Java Runtime Environment to be installed. This can be obtained at: https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html


To run the HamTester Jar in Windows, from the command line run:

runJar.bat

To run the HamTester Jar in Linux, from the command line run:

./runJar.sh





