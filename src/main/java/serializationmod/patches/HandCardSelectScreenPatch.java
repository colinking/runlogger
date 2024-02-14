package serializationmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import serializationmod.GameStateConverter;
import serializationmod.SerializationMod;

import java.util.ArrayList;
import java.util.TreeMap;

public class HandCardSelectScreenPatch {
	// Tracks the state of the hand when HandCardSelectScreen was last opened.
	//
	// This is useful since selecting cards from your hand rearranges the order of your hand. We use
	// this to identify the index of card that was selected.
	private static ArrayList<AbstractCard> handWhenOpened;

	@SpirePatch(
		clz= HandCardSelectScreen.class,
		method="open",
		paramtypez = {String.class, int.class, boolean.class, boolean.class}
	)
	public static class Open1Patch {
		public static void Prefix(HandCardSelectScreen instance, String msg, int amount, boolean anyNumber, boolean canPickZero) {
			// We serialize state separately from serializing the action because selecting cards
			// removes them from the user's hand. Therefore, we must serialize the state before
			// they start selecting cards.
			SerializationMod.run.append(GameStateConverter.getFloorState());
			HandCardSelectScreenPatch.handWhenOpened = new ArrayList<>(AbstractDungeon.player.hand.group);
		}
	}

	@SpirePatch(
		clz= HandCardSelectScreen.class,
		method="open",
		paramtypez = {String.class, int.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class}
	)
	public static class Open2Patch {
		public static void Prefix(HandCardSelectScreen instance, String msg, int amount, boolean anyNumber, boolean canPickZero, boolean forTransform, boolean forUpgrade, boolean upTo) {
			// We serialize state separately from serializing the action because selecting cards
			// removes them from the user's hand. Therefore, we must serialize the state before
			// they start selecting cards.
			SerializationMod.run.append(GameStateConverter.getFloorState());
			HandCardSelectScreenPatch.handWhenOpened = new ArrayList<>(AbstractDungeon.player.hand.group);
		}
	}

	@SpirePatch(
		clz= HandCardSelectScreen.class,
		method="update"
	)
	public static class UpdatePatch {
		@SpireInsertPatch(
			locator= Locator.class
		)
		public static void Insert(HandCardSelectScreen instance) {
			TreeMap<String, Object> action = new TreeMap<>();
			action.put("_type", "action:select_cards");
			ArrayList<TreeMap<String, Object>> cards = new ArrayList<>();
			for (AbstractCard card : instance.selectedCards.group) {
				TreeMap<String, Object> c = new TreeMap<>();
				c.put("name", GameStateConverter.getCardName(card));
				c.put("index", HandCardSelectScreenPatch.handWhenOpened.indexOf(card));
				cards.add(c);
			}
			action.put("cards", cards);

			// Where cards are getting selected from.
			action.put("from", "hand");

			// Print the updated hand; this may have changed due to selecting/unselecting cards.
			ArrayList<TreeMap<String, Object>> newHand = new ArrayList<>();
			for (AbstractCard card : AbstractDungeon.player.hand.group) {
				TreeMap<String, Object> c = new TreeMap<>();
				c.put("name", GameStateConverter.getCardName(card));
				c.put("index", HandCardSelectScreenPatch.handWhenOpened.indexOf(card));
				newHand.add(c);
			}
			action.put("hand", newHand);

			Gson gson = new Gson();
			SerializationMod.run.append(gson.toJson(action));
		}

		private static class Locator extends SpireInsertLocator {
			public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
				Matcher matcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "closeCurrentScreen");
				return LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<Matcher>(), matcher);
			}
		}
	}
}
