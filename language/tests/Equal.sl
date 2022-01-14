/*
 * Copyright (c) 2020, Oracle and/or its affiliates. All rights reserved.
 * Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.
 */

function e(a, b) {
  return a == b;
}

function main() {  
  print(e(4, 4));  
  print(e(3, "aaa"));  
  print(e(4, 4));  
  print(e("a", "a"));  
  print(e(1==2, 1==2));  
  print(e(1==2, 1));  
  print(e(e, e));  
}  
