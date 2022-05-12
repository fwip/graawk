@echo off

IF [[ "%SL_BUILD_NATIVE%" == "false" "]]" (
  echo "Skipping the native image build because SL_BUILD_NATIVE is set to false."
  exit "0"
)
"$JAVA_HOME"/bin/native-image "--macro:truffle" "--no-fallback" "--initialize-at-build-time=com.oracle.truffle.sl,org.antlr.v4" "-cp" "%CD%.\language\target\simplelanguage.jar:%CD%.\launcher\target\sl-launcher.jar" "com.oracle.truffle.sl.launcher.SLMain" "slnative"
