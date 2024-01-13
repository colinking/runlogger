## Building from source

1. Copy over the necessary JARs by running the following commands:

```sh
# Change the pathnames as necessary 
mkdir "/Users/colin/dev/github.com/colinking/serializationmod/lib" 
cp \
  "/Users/colin/Library/Application Support/Steam/steamapps/workshop/content/646570/1605060445/ModTheSpire.jar" \
  "/Users/colin/dev/github.com/colinking/serializationmod/lib"
cp \
  "/Users/colin/Library/Application Support/Steam/steamapps/workshop/content/646570/1605833019/BaseMod.jar" \
  "/Users/colin/dev/github.com/colinking/serializationmod/lib"
cp \
  "/Users/colin/Library/Application Support/Steam/steamapps/common/SlayTheSpire/SlayTheSpire.app/Contents/Resources/desktop-1.0.jar" \
  "/Users/colin/dev/github.com/colinking/serializationmod/lib"
```

2. Build `SerializationMod` by running `mvn package`.

If running in IntelliJ, you may need to "Invalidate caches and restart" to get it to pick up the libraries you copied over in (1).

## Running STS

You need to run STS using Java 8 which is automatically installed by Steam. You can run directly from Steam, but it is faster to boot from the terminal:

```shell
cd ~/Library/Application Support/Steam/steamapps/common/SlayTheSpire/SlayTheSpire.app/Contents/Resources

# You may want to use a dedicated save slot for modding. This command automatically selects
# it before opening STS. Assumes your save slot is the second slot ("1").
cp ~/Library/Application\ Support/Steam/steamapps/common/SlayTheSpire/SlayTheSpire.app/Contents/Resources/preferences/STSSaveSlots \
  ~/Library/Application\ Support/Steam/steamapps/common/SlayTheSpire/SlayTheSpire.app/Contents/Resources/preferences/STSSaveSlots.backUp
jq -r '.DEFAULT_SLOT |= "1"' \
  ~/Library/Application\ Support/Steam/steamapps/common/SlayTheSpire/SlayTheSpire.app/Contents/Resources/preferences/STSSaveSlots.backUp \
  > ~/Library/Application\ Support/Steam/steamapps/common/SlayTheSpire/SlayTheSpire.app/Contents/Resources/preferences/STSSaveSlots

# https://github.com/kiooeht/ModTheSpire/wiki/Command-Line-Arguments
./jre/bin/java \
  -jar ~/Library/Application\ Support/Steam/steamapps/workshop/content/646570/1605060445/ModTheSpire.jar \
  --mods basemod,RunHistoryPlus,superfastmode,SerializationMod,CommunicationMod --skip-launcher --skip-intro
```

Some advice:

- You may also want to configure superfastmode to use 1000% game speed.
- If you hit a NullPointerException, make sure you are in the correct directory.
- For debugging purposes, you can download the Java source code [here](https://hg.openjdk.org/jdk8u/jdk8u-dev/jdk/rev/3ad9fa6a5a13) (based on [these instructions](https://stackoverflow.com/a/54009204)).
- You can access the STS log via `code ~/Library/Application Support/Steam/steamapps/common/SlayTheSpire/SlayTheSpire.app/Contents/Resources/sendToDevs/logs/SlayTheSpire.log` 