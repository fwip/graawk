/*
 * Copyright (c) 2020, Oracle and/or its affiliates. All rights reserved.
 * Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.
 */

function null() {
}

function foo() {
  return "bar";
}

function f(a, b) {
  return a + " < " + b + ": " + (a < b);
}

function main() {  
  print("s" + null());  
  print("s" + null);  
  print("s" + foo());  
  print("s" + foo);
    
  print(null() + "s");  
  print(null() + "s");  
  print(foo() + "s");  
  print(foo + "s");

  print(f(2, 4));
  print(f(2, "4"));
}  
