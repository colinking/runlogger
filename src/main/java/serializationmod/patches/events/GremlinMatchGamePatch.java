package serializationmod.patches.events;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.events.shrines.GremlinMatchGame;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import serializationmod.GameStateConverter;
import serializationmod.SerializationMod;

import java.util.*;

public class GremlinMatchGamePatch {
	public static HashMap<UUID, Integer> cardPositions;
	public static ArrayList<AbstractCard> cards;
	public static Set<UUID> revealedCards;
	public static Set<UUID> matchedCards;

	// This patch is ported from CommunicationMod.
	@SpirePatch(
		clz=GremlinMatchGame.class,
		method=SpirePatch.CONSTRUCTOR
	)
	public static class InitializeCardsPatch {
		public static void Postfix(GremlinMatchGame instance) {
			CardGroup cg = ReflectionHacks.getPrivate(instance, GremlinMatchGame.class, "cards");
			cards = new ArrayList<>(cg.group);
			revealedCards = new HashSet<>();
			matchedCards = new HashSet<>();
			// If 0 is top left and 11 is bottom right, the positions of the cards in the result array are:
			// [0, 5, 10, 3, 4, 9, 2, 7, 8, 1, 6, 11]. We want to store the initial positions for easy reference.
			// Cards can be removed from the card group, so it is easier to just calculate them at the start.
			cardPositions = new HashMap<>();
			for(int i = 0; i < 12; i++) {
				AbstractCard currentCard = cards.get(i);
				int target_x = i % 4;
				int target_y = i % 3;
				int position = target_x + 4 * target_y;
				cardPositions.put(currentCard.uuid, position);
			}
		}
	}

	@SpirePatch(clz = GremlinMatchGame.class, method="updateMatchGameLogic")
	public static class Patch {
		@SpireInsertPatch(
			locator= Locator.class,
			localvars = {"hoveredCard"}
		)
		public static void Insert(GremlinMatchGame instance, AbstractCard hoveredCard) {
			SerializationMod.run.append(GameStateConverter.getFloorState());

			TreeMap<String, Object> action = new TreeMap<>();
			action.put("_type", "action:select_match");
			int position = cardPositions.get(hoveredCard.uuid);
			action.put("card", GameStateConverter.getCardName(hoveredCard));
			action.put("index", position);

			Gson gson = new Gson();
			SerializationMod.run.append(gson.toJson(action));

			revealedCards.add(hoveredCard.uuid);
			AbstractCard chosenCard = ReflectionHacks.getPrivate(instance, GremlinMatchGame.class, "chosenCard");
			if (chosenCard != null && hoveredCard.cardID.equals(chosenCard.cardID)) {
				matchedCards.add(chosenCard.uuid);
				matchedCards.add(hoveredCard.uuid);
			}
		}

		private static class Locator extends SpireInsertLocator {
			public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
				Matcher matcher = new Matcher.FieldAccessMatcher(InputHelper.class, "justClickedLeft");
				int[] lines = LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<Matcher>(), matcher);
				// Select the second field access. This detects when a user clicks a card.
				return new int[]{lines[1]};
			}
		}
	}
}
