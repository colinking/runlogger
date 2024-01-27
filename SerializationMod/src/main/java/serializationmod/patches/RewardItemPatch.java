package serializationmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.google.gson.Gson;
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

		SerializationMod.run.append(GameStateConverter.getFloorState());

		TreeMap<String, Object> action = new TreeMap<>();
		action.put("_type", "action:select_reward");
		action.put("reward", instance.type.name());

		Gson gson = new Gson();
		SerializationMod.run.append(gson.toJson(action));
	}
}

