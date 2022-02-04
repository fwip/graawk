// Test that regexes work properly

function main() {
  print(/a/ ~ "a");
  print(/a/ ~ "bab");
  print(/a/ ~ "bb");
  print(/a/ !~ "a");
  print(/a/ !~ "bab");
  print(/a/ !~ "bb");
}  
