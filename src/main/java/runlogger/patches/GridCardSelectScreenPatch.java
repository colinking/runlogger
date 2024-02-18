package runlogger.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import runlogger.GameStateConverter;
import runlogger.RunLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

public class GridCardSelectScreenPatch {
	@SpirePatch(
		clz= GridCardSelectScreen.class,
		method="update"
	)
	public static class ConfirmationGridPatch {
		@SpireInsertPatch(
			locator= Locator.class
		)
		public static void Insert(GridCardSelectScreen instance) {
			RunLogger.run.append(GameStateConverter.getFloorState());

			TreeMap<String, Object> action = new TreeMap<>();
			action.put("_type", "action:confirm_cards");

			Gson gson = new Gson();
			RunLogger.run.append(gson.toJson(action));
		}

		private static class Locator extends SpireInsertLocator {
			public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
				Matcher matcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "closeCurrentScreen");
				// The first instance is for `isJustForConfirming` (e.g. Pandora's Box).
				return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), matcher);
			}
		}
	}

	@SpirePatch(
		clz= GridCardSelectScreen.class,
		method="update"
	)
	public static class UpdatePatch {
		@SpireInsertPatch(
			locator= Locator.class
		)
		public static void Insert(GridCardSelectScreen instance) {
			RunLogger.run.append(GameStateConverter.getFloorState());

			TreeMap<String, Object> action = new TreeMap<>();
			action.put("_type", "action:select_cards");
			ArrayList<TreeMap<String, Object>> cards = new ArrayList<>();
			for (AbstractCard card : instance.selectedCards) {
				TreeMap<String, Object> c = new TreeMap<>();
				c.put("name", GameStateConverter.getCardName(card));
				c.put("index", instance.targetGroup.group.indexOf(card));
				cards.add(c);
			}
			action.put("cards", cards);

			if (instance.forUpgrade) {
				action.put("select_type", "smith");
			} else if (instance.forPurge) {
				action.put("select_type", "remove");
			} else if (instance.forTransform) {
				action.put("select_type", "transform");
			}
			// TODO: other select types...

			Gson gson = new Gson();
			RunLogger.run.append(gson.toJson(action));
		}

		private static class Locator extends SpireInsertLocator {
			public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
				Matcher matcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "closeCurrentScreen");
				int[] lines = LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<Matcher>(), matcher);
				// The first instance is for `isJustForConfirming` (e.g. Pandora's Box).
				return Arrays.copyOfRange(lines, 1, lines.length);
			}
		}
	}
}
