// Test that regexes work properly

function main() {
  print("a" ~ /a/);
  print("bab" ~ /a/);
  print("bb" ~ /a/);
  print("a" !~ /a/);
  print("bab" !~ /a/);
  print("bb" !~ /a/);
}
