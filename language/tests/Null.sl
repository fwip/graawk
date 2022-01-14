/*
 * Copyright (c) 2020, Oracle and/or its affiliates. All rights reserved.
 * Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.
 */

/* The easiest way to generate null: a function without a return statement implicitly returns null. */
function null() {
}

function main() {  
  print(null());  
  print(null() == null());  
  print(null() != null());  
  print(null() == 42);  
  print(null() != 42);  
  print(null() == "42");  
  print(null() != "42");  
}  
