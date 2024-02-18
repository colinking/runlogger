#!/bin/bash

set -euxo pipefail

SCRIPT_DIR=$(dirname "$0")
cd $SCRIPT_DIR/..
mvn clean
mvn package

# These directories are OS-dependent.
STS_DIR=~/Library/Application\ Support/Steam/steamapps/common/SlayTheSpire/SlayTheSpire.app/Contents/Resources
STEAM_DIR=~/Library/Application\ Support/Steam/steamapps

# We need to run ModTheSpire from this directory, else it can't find the Steam-installed JRE.
# This otherwise produces a NullPointerException.
cd "$STS_DIR"

# You may want to use a dedicated save slot for modding. This command automatically selects
# it before opening STS. Assumes your save slot is the second slot ("1").
cp preferences/STSSaveSlots preferences/STSSaveSlots.backUp
jq -r '.DEFAULT_SLOT |= "1"' preferences/STSSaveSlots.backUp > preferences/STSSaveSlots

# These mods are always enabled.
MODS="basemod,determinismfix,runlogger"
# Additional mods can be passed in as $1.
if [ ! -z "${1:-}" ]; then
  MODS="${MODS},$1"
fi

# Set to "true" to debug patches. Slows down startup time.
DEBUG=false

# https://github.com/kiooeht/ModTheSpire/wiki/Command-Line-Arguments
./jre/bin/java \
  -jar "$STEAM_DIR/workshop/content/646570/1605060445/ModTheSpire.jar" \
  --mods $MODS --skip-launcher --skip-intro --debug=$DEBUG \
  > ./modthespire.log
