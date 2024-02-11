#!/bin/bash

set -euxo pipefail

cd /Users/colin/dev/github.com/colinking/serializationmod/SerializationMod
mvn clean
mvn package

STS_FOLDER=~/Library/Application\ Support/Steam/steamapps/common/SlayTheSpire/SlayTheSpire.app/Contents/Resources

# We need to run ModTheSpire from this directory, else it can't find the Steam-installed JRE.
# This otherwise produces a NullPointerException.
cd "$STS_FOLDER"

# You may want to use a dedicated save slot for modding. This command automatically selects
# it before opening STS. Assumes your save slot is the second slot ("1").
cp preferences/STSSaveSlots preferences/STSSaveSlots.backUp
jq -r '.DEFAULT_SLOT |= "1"' preferences/STSSaveSlots.backUp > preferences/STSSaveSlots

# SaveStateMod,stslib,LudicrousSpeed
# ojb_FilterTheSpire
MODS="basemod,determinismfix,serializationmod"
if [ ! -z "${1:-}" ]; then
  MODS="${MODS},$1"
fi

# Set to "true" to debug patches. Slows down startup time.
DEBUG=false

# https://github.com/kiooeht/ModTheSpire/wiki/Command-Line-Arguments
./jre/bin/java \
  -jar ~/Library/Application\ Support/Steam/steamapps/workshop/content/646570/1605060445/ModTheSpire.jar \
  --mods $MODS --skip-launcher --skip-intro --debug=$DEBUG \
  > ./modthespire.log