package serializationmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.shrines.PurificationShrine;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import serializationmod.GameStateConverter;
import serializationmod.SerializationMod;

import java.util.ArrayList;
import java.util.TreeMap;

public class PurificationShrinePatch {
	@SpirePatch(
		clz= PurificationShrine.class,
		method="update"
	)
	public static class UpdatePatch {
		@SpireInsertPatch(
			locator= Locator.class
		)
		public static void Insert(PurificationShrine instance) {
			SerializationMod.run.append(GameStateConverter.getFloorState());

			TreeMap<String, Object> action = new TreeMap<>();
			action.put("_type", "action:remove_cards");
			ArrayList<String> cards = new ArrayList<>();
			for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
				cards.add(GameStateConverter.getCardName(card));
			}
			action.put("cards", cards);

			Gson gson = new Gson();
			SerializationMod.run.append(gson.toJson(action));
		}

		private static class Locator extends SpireInsertLocator {
			public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
				Matcher matcher = new Matcher.MethodCallMatcher(PurificationShrine.class, "logMetricCardRemoval");
				return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), matcher);
			}
		}
	}
}
