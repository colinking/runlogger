package serializationmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import serializationmod.GameStateConverter;
import serializationmod.SerializationMod;

import java.util.TreeMap;

@SuppressWarnings("unused")
public class GameActionManagerPatch {
	public static boolean performedActions = false;

	@SpirePatch(
		clz= GameActionManager.class,
		method="getNextAction"
	)
	public static class GetNextActionPatch {
		public static void Prefix(GameActionManager instance) {
			if (!instance.actions.isEmpty()) {
				SerializationMod.logger.info("GetNextActionPatch: performing action " + instance.actions.get(0).toString());
			} else if (!instance.preTurnActions.isEmpty()) {
				SerializationMod.logger.info("GetNextActionPatch: performing pre-turn action " + instance.preTurnActions.get(0).toString());
			}

			boolean hasActions = !instance.actions.isEmpty() || !instance.preTurnActions.isEmpty();
			if (hasActions) {
				// if (!performedActions) {
				// 	// We've seen an action and therefore need to print out the state
				// 	// the next time the action queue is empty.
				// 	performedActions = true;
				// }

				return;
			}

			if (performedActions) {
				SerializationMod.logger.info("GetNextActionPatch: actions queue now empty");

				// The action queue is empty and we performed actions since the last time we
				// printed state. Therefore, we need to print the state.
				// performedActions = false;

				// SerializationMod.run.append(GameStateConverter.getFloorState());
			}

			if (instance.cardQueue.size() > 0) {
				if (instance.cardQueue.get(0).card == null) {
					SerializationMod.run.append(GameStateConverter.getFloorState());

					// 	The turn is ending.
					TreeMap<String, Object> action = new TreeMap<>();
					action.put("_type", "action:end_turn");

					Gson gson = new Gson();
					SerializationMod.run.append(gson.toJson(action));
				} else if (!instance.cardQueue.get(0).autoplayCard && !instance.cardQueue.get(0).isEndTurnAutoPlay) {
					SerializationMod.run.append(GameStateConverter.getFloorState());

					// A card is getting played. Note if the first card in the queue is null, then the
					// turn has ended.
					CardQueueItem item = instance.cardQueue.get(0);
					TreeMap<String, Object> action = new TreeMap<>();
					action.put("_type", "action:play_card");
					action.put("card", GameStateConverter.getCardName(item.card));
					action.put("card_index", AbstractDungeon.player.hand.group.indexOf(item.card));
					if (item.monster != null) {
						action.put("target", item.monster.id);
						action.put("target_index", AbstractDungeon.getMonsters().monsters.indexOf(item.monster));
					}

					Gson gson = new Gson();
					SerializationMod.run.append(gson.toJson(action));
				}
			}
		}
	}
}
