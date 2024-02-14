# Contributing

## Building from source

To build this mod from source, run `mvn package`. This will automatically copy the JAR into the STS mods folder. Make sure to enable Serialization Mod in the ModTheSpire menu.

## Running STS (faster)

It is faster to start STS via the ModTheSpire launcher directly (instead of via Steam).

This repo includes a script that handles that for you -- just run `./scripts/run.sh`.  

Keep in mind:

- This assumes you have a save slot for modding, specifically the second save slot. This script automatically switches
  to that save slot to avoid polluting your main save slot.
- You can access the STS log via `<sts_dir>/sendToDevs/logs/SlayTheSpire.log` which includes any logs produced by this mod.

## Replaying run logs

You can replay a run log using [CommunicationMod](https://steamcommunity.com/workshop/filedetails/?id=2131373661), however the tooling is in another repo. I'm planning to move that here soon.

Keep in mind:

- You should use a separate save slot, otherwise you'll pollute your run history with replays.
- If debugging a replay via CommunicationMod, errors will be printed in `<sts_dir>/communication_mod_errors.log`
- You may also want to configure superfastmode to use 1000% game speed.
- You will need to enable all unlocks -- see below.

### Unlocks

These files all live in your STS directory.

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