#!/usr/bin/env bash

# ---------- config ----------
MAIN=atlas.Atlas
SAVE_FILE="data/Atlas.txt"   # must match the path you use in atlas.Storage
BIN="../bin"
SRC="../src/main/java"

# ---------- helpers ----------
normalize () {
  # normalize line endings -> UNIX
  # usage: normalize <in> <out>
  if command -v dos2unix >/dev/null 2>&1; then
    cp "$1" "$2"
    dos2unix "$2" >/dev/null 2>&1
  else
    # fallback if dos2unix isn't installed
    sed $'s/\r$//' "$1" > "$2"
  fi
}

# ---------- setup ----------
[ -d "$BIN" ] || mkdir "$BIN"

rm -f ACTUAL1.TXT ACTUAL2.TXT ACTUAL-SAVE.TXT \
      ACTUAL2-UNIX.TXT ACTUAL-SAVE-UNIX.TXT \
      EXPECTED2-UNIX.TXT EXPECTED-SAVE-UNIX.TXT

# start from clean storage (first-run scenario)
rm -rf data

# ---------- compile ----------
if ! javac -Xlint:none -d ../bin ../src/main/java/atlas/*.java;
then
  echo "********** BUILD FAILURE **********"
  exit 1
fi

# ---------- RUN #1: create + save ----------
java -classpath ../bin atlas.Atlas < input1.txt > ACTUAL.TXT

# capture save file produced by run #1
if [ -f "$SAVE_FILE" ]; then
  cp "$SAVE_FILE" ACTUAL-SAVE.TXT
else
  echo "Save file not found!" > ACTUAL-SAVE.TXT
fi

# ---------- RUN #2: load + list ----------
java -classpath "$BIN" "$MAIN" < input2.txt > ACTUAL2.TXT

# ---------- normalize line endings for diffs ----------
normalize ACTUAL2.TXT           ACTUAL2-UNIX.TXT
normalize EXPECTED2.TXT         EXPECTED2-UNIX.TXT
normalize ACTUAL-SAVE.TXT       ACTUAL-SAVE-UNIX.TXT
normalize EXPECTED-SAVE.TXT     EXPECTED-SAVE-UNIX.TXT

# ---------- compare ----------
echo "Comparing save file..."
diff ACTUAL-SAVE-UNIX.TXT EXPECTED-SAVE-UNIX.TXT || { echo "Test result: FAILED"; exit 1; }

echo "Comparing second run output..."
diff ACTUAL2-UNIX.TXT EXPECTED2-UNIX.TXT || { echo "Test result: FAILED"; exit 1; }

echo "Test result: PASSED"
