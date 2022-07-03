set antlr-version=4.9.2
set jarfile="antlr-%antlr-version%-complete.jar"

if not exist "%jarfile%" ( curl -O "https://www.antlr.org/download/%jarfile%" )
java -cp "%jarfile%" org.antlr.v4.Tool -package com.oracle.truffle.sl.parser -no-listener language/src/main/java/com/oracle/truffle/sl/parser/SimpleLanguage.g4
