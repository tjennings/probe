# --------------------------------------------------
# Watchr Rules
# --------------------------------------------------
watch( "^src/.*\.clj"   ) { |m| system("time ./bin/runtests.sh")}
watch( "^test/.*\.clj"   ) { |m| system("time ./bin/runtests.sh")}

# --------------------------------------------------
# Signal Handling
# --------------------------------------------------
# Ctrl-C
Signal.trap('INT') { abort("\n") }

