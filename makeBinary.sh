#!/bin/bash

# making a binary requires gcj 4.4.0, which can be obtained at http://mirrors.concertpass.com/gcc/releases/gcc-4.4.0/ or any other gcc mirror site
cd src
gcj *.java --main=HamTester -o ../dist/HamTester
