/*
 * Copyright (c) 2020, Oracle and/or its affiliates. All rights reserved.
 * Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.
 */

function main() {  
  obj1 = new();
  obj1["x"] = 42;
  print(obj1["x"]);
  
  obj2 = new();
  obj2["o"] = obj1;
  print(obj2["o"]["x"]);
  obj2["o"]["y"] = "why";
  print(obj1["y"]);
  
  print(mkobj()["z"]);
  
  obj3 = new();
  obj3["fn"] = mkobj;
  print(obj3["fn"]()["z"]);

  obj4 = new();
  write(obj4, "prop", 1);
  read(obj4, "prop");
  write(obj4, "prop", 2);
  read(obj4, "prop");
  write(obj4, "prop", "three");
  read(obj4, "prop");
  
  obj5 = new();
  i = 1;
  obj5.prop0 = 1;
  while (i < 10) {
    write(obj5, "prop" + i, read(obj5, "prop" + (i - 1)) * 2);
    i = i + 1;
  }
  print(obj5.prop2);
  print(obj5.prop9);

  obj6 = new();
  print(obj6["x"]);
}

function mkobj() {
  newobj = new();
  newobj["z"] = "zzz";
  return newobj;
}

function read(obj, name) {
  return obj[name];
}

function write(obj, name, value) {
  return obj[name] = value;
}
