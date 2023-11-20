#!/bin/sh -f

OS=$(uname)

# set up environment
classPath="$PWD/target/*:$PWD/target/classes/*"

# run 
java -Xmx1536m -Xms1024m -cp $classPath org.jlab.clas.analysis.plots.LambdaKAnalyzer $*
