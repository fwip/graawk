/*
 * Copyright (c) 2020, Oracle and/or its affiliates. All rights reserved.
 * Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.
 */

function doIt(a) {
  print("Initial stack trace:");
  print(stacktrace());
  
  hello = 123;
  print("After 123 assignment:");
  print(stacktrace());
  
  helloEqualsWorld();
  print("After hello assignment:");
  print(stacktrace());
  
//  readln();
}

function main() {
  i = 0;
  while (i < 10) {
    doIt(i);
    i = i + 1;
  }
}
