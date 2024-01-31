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

Run `./run.sh`.

Some advice:

- You may also want to configure superfastmode to use 1000% game speed.
- For debugging purposes, you can download the Java source code [here](https://hg.openjdk.org/jdk8u/jdk8u-dev/jdk/rev/3ad9fa6a5a13) (based on [these instructions](https://stackoverflow.com/a/54009204)).
- You can access the STS log via `code ~/Library/Application Support/Steam/steamapps/common/SlayTheSpire/SlayTheSpire.app/Contents/Resources/sendToDevs/logs/SlayTheSpire.log`

You'll also want to enable all unlocks:

```sh
# Unlock ascension mode + ascension 20
# preferences/1_STSDataVagabond (Ironclad)
{
  "WIN_COUNT": "1",
  "ASCENSION_LEVEL": "20"
}

# preferences/1_STSDataTheSilent
{
  "WIN_COUNT": "1",
  "ASCENSION_LEVEL": "20"
}

# preferences/1_STSDataDefect
{
  "WIN_COUNT": "1",
  "ASCENSION_LEVEL": "20"
}

# preferences/1_STSDataWatcher
{
  "WIN_COUNT": "1",
  "ASCENSION_LEVEL": "20"
}

# Unlock cards and relics
# preferences/1_STSUnlockProgress
{
  "IRONCLADUnlockLevel": "5",
  "IRONCLADProgress": "674",
  "IRONCLADCurrentCost": "2500",
  "IRONCLADTotalScore": "2000",
  "IRONCLADHighScore": "2000",
  "THE_SILENTProgress": "556",
  "THE_SILENTTotalScore": "2000",
  "THE_SILENTHighScore": "2000",
  "THE_SILENTUnlockLevel": "5",
  "THE_SILENTCurrentCost": "2500",
  "DEFECTUnlockLevel": "5",
  "DEFECTProgress": "728",
  "DEFECTCurrentCost": "2500",
  "DEFECTTotalScore": "2000",
  "DEFECTHighScore": "2000",
  "WATCHERProgress": "57",
  "WATCHERTotalScore": "2000",
  "WATCHERHighScore": "2000",
  "WATCHERUnlockLevel": "5",
  "WATCHERCurrentCost": "2500"
}

# Unlock all bosses
# preferences/1_STSSeenBosses
{
  "GUARDIAN": "1",
  "CHAMP": "1",
  "CROW": "1",
  "GHOST": "1",
  "SLIME": "1",
  "AUTOMATON": "1",
  "COLLECTOR": "1",
  "DONUT": "1",
  "WIZARD": "1"
}

# Unlock cards and relics.
# preferences/1_STSUnlocks
{
  "GUARDIAN": "2",
  "CHAMP": "2",
  "CROW": "2",
  "Heavy Blade": "2",
  "Spot Weakness": "2",
  "Limit Break": "2",
  "The Silent": "2",
  "Defect": "2",
  "GHOST": "2",
  "SLIME": "2",
  "Bane": "2",
  "Catalyst": "2",
  "Corpse Explosion": "2",
  "Omamori": "2",
  "Prayer Wheel": "2",
  "Shovel": "2",
  "COLLECTOR": "2",
  "DONUT": "2",
  "Watcher": "2",
  "AUTOMATON": "2",
  "Wild Strike": "2",
  "Evolve": "2",
  "Immolate": "2",
  "WIZARD": "2",
  "Havoc": "2",
  "Sentinel": "2",
  "Exhume": "2",
  "Cloak And Dagger": "2",
  "Accuracy": "2",
  "Storm of Steel": "2",
  "Turbo": "2",
  "Sunder": "2",
  "Meteor Strike": "2",
  "Blue Candle": "2",
  "Dead Branch": "2",
  "Singing Bowl": "2",
  "Art of War": "2",
  "The Courier": "2",
  "Pandora\u0027s Box": "2",
  "Rebound": "2",
  "Undo": "2",
  "Echo Form": "2",
  "Du-Vu Doll": "2",
  "Smiling Mask": "2",
  "Tiny Chest": "2",
  "Cables": "2",
  "Turnip": "2",
  "Runic Capacitor": "2",
  "Hyperbeam": "2",
  "Recycle": "2",
  "Core Surge": "2",
  "Concentrate": "2",
  "Setup": "2",
  "Grand Finale": "2",
  "ForeignInfluence": "2",
  "Alpha": "2",
  "MentalFortress": "2",
  "Prostrate": "2",
  "Blasphemy": "2",
  "Devotion": "2",
  "Akabeko": "2",
  "Yang": "2",
  "CeramicFish": "2",
  "SpiritShield": "2",
  "Wish": "2",
  "Wireheading": "2",
  "StrikeDummy": "2",
  "TeardropLocket": "2",
  "CloakClasp": "2",
  "Emotion Chip": "2",
  "Symbiotic Virus": "2",
  "DataDisk": "2"
}

# Unlock the final act
# preferences/1_STSPlayer
{
  "IRONCLAD_WIN": "true",
  "THE_SILENT_WIN": "true",
  "DEFECT_WIN": "true"
}
```