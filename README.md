# Serialization Mod

This [Slay the Spire](https://store.steampowered.com/app/646570/Slay_the_Spire/) mod generates a detailed log for each
run, including all actions taken (such as playing a card or responding to an event dialog) along with state of the game
at each point.

```jsonc
// saves/IRONCLAD.run.log
{"_type":"state:run","ascension":0,"class":"IRONCLAD","seed":"1SK2FJMAA7GY1", /** ... */}
// ...
{"_type":"action:select_map","symbol":"M","x":6,"y":0}
{"_type":"state:floor","combat_state":{"hand":[/** ... */],"monsters": [/** ... */], /** ... */}}
{"_type":"action:play_card","card":"Strike","card_index":1,"target":"FuzzyLouseDefensive","target_index":0}
{"_type":"state:floor","combat_state":{"discard_pile":["Strike"],"hand":[/** ... */],"monsters": [/** ... */], /** ... */}}
// ...
```

Using run logs, you can perform more detailed data analysis compared to `.run` files (which only contains per-floor
metrics), e.g,:

1. How often you play specific cards against each enemy  
2. How often you "brick" a draw
3. How long it takes to get different decks online

Additionally, these run logs can be used as test cases for Slay the Spire ports to ensure they have identical behavior
to the PC implementation.

This mod is primarily intended for creating conformance tests for Slay the Spire ports (e.g. to create a 100% compatible
Python port that could be used for training AIs). It can also be used for performing much more detailed data analysis
than what can be done with normal `.run` files.

## Get started

Install this mod by subscribing to it in the [Steam workshop](https://steamcommunity.com/workshop/filedetails/?id=3156775649).

> [!NOTE]  
> This mod is currently in **beta**: the run log format may change and you may encounter bugs.
>
> If you run into any issues, notice any discrepancies in your run logs, or have any feedback, please
> [open an issue on GitHub](https://github.com/colinking/serializationmod/issues/new)!

> [!NOTE]  
> Due to a bug in Slay the Spire, runs are not always reproducible. This can be fixed by installing
> [Determinism Fix](https://github.com/colinking/determinismfix).

## Where are my run logs?

Run logs are stored in a `.run.log` file in your Slay the Spire directory.

Until a run is finished, the log file is stored next to the run's save file (e.g. `saves/IRONCLAD.run.log`).

Once the run finishes, the log is renamed to match the `.run` file and moved to a `runlogs` folder in your Slay the
Spire directory (e.g. `runlogs/IRONCLAD/1707684719.run.log`). 

## Log format

Each line in the file is a JSON object containing a `_type` field indicating what the object represents.

There are three kinds of "state" objects:
- `_type: "state:run"`: Contains run-level data, e.g. the seed and character. Always the first line of the file.
- `_type: "state:act"`: Contains act-level data, e.g. the map and boss. Printed at the beginning of each act.
- `_type: "state:floor"`: Contains floor-level data and depends on the kind of floor (e.g. dialog options for events, 
  deck/enemies/etc. for combats).

The rest of the logs represent actions and each will include additional information describing the action.
- `_type: "action:play_card"`: A card was played during combat.
- `_type: "action:select_map"`: The player navigated to a new floor.
- (etc.)

For more information on the format of each object type, see the [runs](./runs) folder for example run logs.

## Contributing

For information about developing on Serialization Mod, see [CONTRIBUTING.md](.github/CONTRIBUTING.md).
