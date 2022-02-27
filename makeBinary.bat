
rem Compiling a binary from Java requires gcj 4.3.0, which can be obtained from: https://www.thisiscool.com/gcc_mingw.htm
cd src
gcj *.java --main=HamTester -o ..\dist\HamTester.exe
