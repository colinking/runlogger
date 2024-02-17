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
metrics), such as:

1. How often you play specific cards against each enemy  
2. How often you "brick" a draw
3. How long it takes to get different decks online
4. (etc.)

These run logs can also be used as test cases for Slay the Spire ports to ensure they have identical behavior to the PC
implementation.

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

> [!NOTE]  
> Until this mod reaches 1.0.0, there may be breaking changes to the run log format.

There are three kinds of "state" objects:
- `_type: "state:run"`: Contains run-level data, e.g. the seed and character. Always the first line of the file.
- `_type: "state:act"`: Contains act-level data, e.g. the map and boss. Printed at the beginning of each act.
- `_type: "state:floor"`: Contains floor-level data and depends on the kind of floor (e.g. dialog options for events, 
  deck/enemies/etc. for combats).
- `_type: "state:game_over"`: Indicates the game ended (either from an abandon, death, or victory). Always the final
  line. Includes the final score.

The rest of the logs represent actions and each will include additional information describing the action.
- `_type: "action:abandon"`: The run was abandoned.
- `_type: "action:bowl"`: A card reward was skipped in favor of using the Singing Bowl.
- `_type: "action:buy"`: Something was purchased from the store.
- `_type: "action:confirm_cards"`: A set of cards were confirmed before adding to the deck (e.g. Pandora's Box).
- `_type: "action:dig"`: A relic was dug up from a rest site.
- `_type: "action:discard_potion"`: A potion was discarded.
- `_type: "action:end_turn"`: The player (manually) ended their turn.
- `_type: "action:lift"`: The player lifted at a rest site.
- `_type: "action:open_chest"`: A non-boss treasure chest was opened.
- `_type: "action:play_card"`: A card was played during combat.
- `_type: "action:recall"`: The player recalled at a rest site.
- `_type: "action:rest"`: The player rested at a rest site.
- `_type: "action:select_boss_relic"`: A boss relic was selected.
- `_type: "action:select_card"`: A card was selected from a card reward.
- `_type: "action:select_cards"`: Zero or more cards were selected from a selection screen (e.g. discarding from hand).
- `_type: "action:select_dialog"`: A dialog option in an event was selected.
- `_type: "action:select_map"`: The player navigated to a new floor.
- `_type: "action:select_match"`: A potential match was selected during the Match and Keep event.
- `_type: "action:select_reward"`: A combat reward was selected (gold, relic, etc.).
- `_type: "action:skip"`: The player skipped a one-time card reward (e.g. discovery/codex) or skipped the boss relic. 
- `_type: "action:spin"`: The player spun the wheel in the Wheel of Change event.
- `_type: "action:use_potion"`: The player used a potion.

For more information on the format of each object type, see the [runs](./runs) folder for example run logs.

## Contributing

For information about developing on Serialization Mod, see [CONTRIBUTING.md](.github/CONTRIBUTING.md).
