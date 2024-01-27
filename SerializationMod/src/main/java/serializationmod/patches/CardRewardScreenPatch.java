package serializationmod.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.ui.buttons.SkipCardButton;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import serializationmod.GameStateConverter;
import serializationmod.SerializationMod;

import java.util.ArrayList;
import java.util.TreeMap;

public class CardRewardScreenPatch {
	@SpirePatch(
		clz= CardRewardScreen.class,
		method = "cardSelectUpdate"
	)
	public static class AcquireCardPatch {
		@SpireInsertPatch(
			locator=Locator.class,
			localvars = {"hoveredCard"}
		)
		public static void Insert(CardRewardScreen instance, AbstractCard hoveredCard) {
			SerializationMod.run.append(GameStateConverter.getFloorState());

			TreeMap<String, Object> action = new TreeMap<>();
			action.put("_type", "action:select_card");

			boolean discovery = ReflectionHacks.getPrivate(instance, CardRewardScreen.class, "discovery");
			boolean codex = ReflectionHacks.getPrivate(instance, CardRewardScreen.class, "codex");
			boolean chooseOne = ReflectionHacks.getPrivate(instance, CardRewardScreen.class, "chooseOne");
			if (discovery) {
				action.put("card_kind", "discovery");
			} else if (codex) {
				action.put("card_kind", "codex");
			} else if (chooseOne) {
				// Note: these are not "cards"; this is wish/stance potion
				action.put("card_kind", "special");
			} else {
				action.put("card_kind", "combat_reward");
			}


			action.put("card", GameStateConverter.getCardName(hoveredCard));
			int cardIndex = -1;
			for (int i = 0; i < instance.rewardGroup.size(); i++) {
				if (instance.rewardGroup.get(i).uuid.equals(hoveredCard.uuid)) {
					cardIndex = i;
					break;
				}
			}
			action.put("card_index", cardIndex);

			// TODO: indicate which combat card reward this is, if there are multiple (prayer wheel)

			Gson gson = new Gson();
			SerializationMod.run.append(gson.toJson(action));
		}

		private static class Locator extends SpireInsertLocator {
			public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
				Matcher matcher = new Matcher.MethodCallMatcher(SkipCardButton.class, "hide");
				return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), matcher);
			}
		}
	}
}
