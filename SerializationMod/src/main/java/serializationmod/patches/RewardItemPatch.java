package serializationmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.rewards.RewardItem;
import serializationmod.GameStateConverter;
import serializationmod.SerializationMod;

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
		if (instance.type == RewardItem.RewardType.POTION) {
			if (AbstractDungeon.player.hasRelic("Sozu")) {
				// The player cannot acquire this potion.
				return;
			}
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

		SerializationMod.run.append(GameStateConverter.getFloorState());

		TreeMap<String, Object> action = new TreeMap<>();
		action.put("_type", "action:select_reward");
		action.put("reward", instance.type.name());

		Gson gson = new Gson();
		SerializationMod.run.append(gson.toJson(action));
	}
}

