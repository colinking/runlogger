package runlogger.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.rewards.RewardItem;
import runlogger.GameStateConverter;
import runlogger.RunLogger;

import java.util.TreeMap;

@SpirePatch(
	clz= RewardItem.class,
	method="claimReward"
)
public class RewardItemPatch {
	public static void Prefix(RewardItem instance) {
		if (instance.type == RewardItem.RewardType.CARD) {
			// Selecting a card does not impact the state of the game.
			return;
		}
		if (instance.ignoreReward) {
			// The linked reward (e.g. sapphire key or relic) was selected instead.
			return;
		}
		// If the player does not have any open potion slots, claimReward is a no-op.
		// Unless the user has Sozu, in which case the reward disappears.
		if (instance.type == RewardItem.RewardType.POTION && !AbstractDungeon.player.hasRelic("Sozu")) {
			boolean hasOpenSlots = false;
			for (AbstractPotion potion : AbstractDungeon.player.potions) {
				if (potion instanceof PotionSlot) {
					hasOpenSlots = true;
					break;
				}
			}
			if (!hasOpenSlots) {
				// The player cannot acquire this potion.
				return;
			}
		}

		RunLogger.run.append(GameStateConverter.getFloorState());

		TreeMap<String, Object> action = new TreeMap<>();
		action.put("_type", "action:select_reward");
		action.put("reward", instance.type.name());
		action.put("reward_index", AbstractDungeon.combatRewardScreen.rewards.indexOf(instance));

		Gson gson = new Gson();
		RunLogger.run.append(gson.toJson(action));
	}
}

