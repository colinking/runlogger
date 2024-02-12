# Serialization Mod

This [Slay the Spire](https://store.steampowered.com/app/646570/Slay_the_Spire/) mod generates a detailed log for each
run, including all actions taken (such as playing a card or responding to an event dialog) along with state of the game
at each point.

```json
// saves/IRONCLAD.run.log
{"_type":"state:run","ascension":0,"class":"IRONCLAD","seed":"1SK2FJMAA7GY1", /** ... */}
// ...
{"_type":"action:select_map","symbol":"M","x":6,"y":0}
{"_type":"state:floor","combat_state":{"hand":[/** ... */],"monsters": [/** ... */], /** ... */}}
{"_type":"action:play_card","card":"Strike","card_index":1,"target":"FuzzyLouseDefensive","target_index":0}
{"_type":"state:floor","combat_state":{"discard_pile":["Strike"],"hand":[/** ... */],"monsters": [/** ... */], /** ... */}}
// ...
```

This mod is primarily intended for creating conformance tests for Slay the Spire ports (e.g. to create a 100% compatible
Python port that could be used for training AIs). It can also be used for performing much more detailed data analysis
than what can be done with normal `.run` files.

## Installation

Install this mod by subscribing to it in the [Steam workshop](https://steamcommunity.com/workshop/filedetails/?id=3156775649).

## Log format

Run logs are stored in a `.run.log` file.

TODO

## Overview

- As you play STS, a run log is created that tracks all actions taken in the run and the state of the game in-between.
- Unlike the vanilla `.run` file or Run History Plus's `.run` file which record aggregate stats per floor, this records everything that happened in the run.
- This can be used for a variety of use-cases, primarily:
  - Conformance testing for porting STS
  - Data analysis
- This mod can also be used to "replay" a run, e.g. to return to an arbitrary point and try different actions. However,
  that can likely be better suited by other mods, e.g. Save State.
- For examples, see the `runs/` folder.
- Logs are stored in your STS directory. Until a run completes, they are stored next to the corresponding savefile (e.g. `saves/WATCHER.run.log`). Once a run is completed

- This mod is in beta. If you run into any issues, please report them on GitHub. While in beta, the format of the run logs may change.
- Recommend also running Determinism Fix. Does not impact competitive integrity of the game.
- This mod is compatible with other QOL mods (e.g. Run History Plus), but it does not support mods that introduce new game mechanics as it only detects vanilla actions.

## Contributing

For information about developing on Serialization Mod, see [CONTRIBUTING.md](.github/CONTRIBUTING.md).
