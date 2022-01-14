/*
 * Copyright (c) 2020, Oracle and/or its affiliates. All rights reserved.
 * Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.
 */

function invoke(f) {
  f("hello");
}

function f1() {
  print("f1");
}

function f2() {
  print("f2");
}

function f3() {
  print("f3");
}

function f4() {
  print("f4");
}

function f5() {
  print("f5");
}

function main() {
  invoke(f1);
  invoke(f2);
  invoke(f3);
  invoke(f4);
  invoke(f5);
  invoke(foo);  
}  
