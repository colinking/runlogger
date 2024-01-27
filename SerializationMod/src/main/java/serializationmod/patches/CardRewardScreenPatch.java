package serializationmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import serializationmod.GameStateConverter;
import serializationmod.SerializationMod;

import java.util.TreeMap;

public class CardRewardScreenPatch {
	@SpirePatch(
		clz= CardRewardScreen.class,
		method = "acquireCard"
	)
	public static class AcquireCardPatch {
		public static void Prefix(CardRewardScreen instance, AbstractCard hoveredCard) {
			SerializationMod.run.append(GameStateConverter.getFloorState());

			TreeMap<String, Object> action = new TreeMap<>();
			action.put("_type", "action:select_reward");
			action.put("reward", "CARD");
			action.put("card", GameStateConverter.getCardName(hoveredCard));
			int cardIndex = -1;
			for (int i = 0; i < instance.rewardGroup.size(); i++) {
				if (instance.rewardGroup.get(i).uuid.equals(hoveredCard.uuid)) {
					cardIndex = i;
					break;
				}
			}
			action.put("card_index", cardIndex);

			// TODO: indicate which card reward this is, if there are multiple (prayer wheel)

			Gson gson = new Gson();
			SerializationMod.run.append(gson.toJson(action));
		}
	}
}
