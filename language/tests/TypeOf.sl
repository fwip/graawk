/*
 * Copyright (c) 2020, Oracle and/or its affiliates. All rights reserved.
 * Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.
 */

function main() {
  print(typeOf(42));
  print(typeOf(42000000000000000000000000000000000000000));
  print(typeOf("42"));
  print(typeOf(42 == 42));
  print(typeOf(new()));
  print(typeOf(main));
  print(typeOf(null()));
}
function null() {
}
