/*
 * Copyright (c) 2020, Oracle and/or its affiliates. All rights reserved.
 * Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.
 */

function main() {
  t = 10 == 10; // true
  f = 10 != 10; // false
  print(left(f) && right(f));
  print(left(f) && right(t));
  print(left(t) && right(f));
  print(left(t) && right(t));
  print("");
  print(left(f) || right(f));
  print(left(f) || right(t));
  print(left(t) || right(f));
  print(left(t) || right(t));
}

function left(x) {
  print("left");
  return x;
}

function right(x) {
  print("right");
  return x;
}
