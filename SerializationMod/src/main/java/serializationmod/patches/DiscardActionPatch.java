package serializationmod.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import serializationmod.GameStateConverter;
import serializationmod.SerializationMod;

import java.util.ArrayList;
import java.util.TreeMap;

public class DiscardActionPatch {
	@SpirePatch(
		clz= DiscardAction.class,
		method="update"
	)
	public static class UpdateActionPatch {
		@SpireInsertPatch(
			locator= Locator.class
		)
		public static void Insert(DiscardAction instance) {
			boolean isRandom = ReflectionHacks.getPrivate(instance, DiscardAction.class, "isRandom");
			boolean endTurn = ReflectionHacks.getPrivate(instance, DiscardAction.class, "endTurn");
			if (isRandom || endTurn) {
				return;
			}

			TreeMap<String, Object> action = new TreeMap<>();
			action.put("_type", "action:discard_card");
			ArrayList<String> cards = new ArrayList<>();
			for (AbstractCard card : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
				cards.add(GameStateConverter.getCardName(card));
			}
			action.put("cards", cards);

			// A card can be selected and then unselected, which moves that card to the end of your hand.
			// Therefore, it's possible to reorder your hand while discarding. We print the new hand to
			// show any of these movements that may have happened.
			ArrayList<String> hand = new ArrayList<>();
			for (AbstractCard card : AbstractDungeon.player.hand.group) {
				hand.add(GameStateConverter.getCardName(card));
			}
			action.put("hand", hand);

			Gson gson = new Gson();
			SerializationMod.run.append(gson.toJson(action));
		}

		private static class Locator extends SpireInsertLocator {
			public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
				Matcher matcher = new Matcher.FieldAccessMatcher(CardGroup.class, "group");
				return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), matcher);
			}
		}
	}
}
