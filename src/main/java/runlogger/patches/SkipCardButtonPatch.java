package runlogger.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.ui.buttons.SkipCardButton;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import runlogger.GameStateConverter;
import runlogger.RunLogger;

import java.util.ArrayList;
import java.util.TreeMap;

public class SkipCardButtonPatch {
	@SpirePatch(
		clz= SkipCardButton.class,
		method = "update"
	)
	public static class UpdatePatch {
		@SpireInsertPatch(
			locator= Locator.class
		)
		public static void Insert(SkipCardButton instance) {
			SkipCardButtonPatch.recordSkipAction();
		}

		private static class Locator extends SpireInsertLocator {
			public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
				Matcher matcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "closeCurrentScreen");
				return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), matcher);
			}
		}
	}

	public static void recordSkipAction() {
		// You can skip a card reward, but it is sometimes a permanent action depending
		// on whether the card reward screen can be re-opened. For example, skipping
		// a discovery reward is permanent while skipping a combat card reward is not.
		//
		// Only serialize this as an action if it is permanent.
		boolean discovery = ReflectionHacks.getPrivate(AbstractDungeon.cardRewardScreen, CardRewardScreen.class, "discovery");
		boolean codex = ReflectionHacks.getPrivate(AbstractDungeon.cardRewardScreen, CardRewardScreen.class, "codex");
		if (!discovery && !codex) {
			return;
		}

		RunLogger.run.append(GameStateConverter.getFloorState());

		TreeMap<String, Object> action = new TreeMap<>();
		action.put("_type", "action:skip");

		Gson gson = new Gson();
		RunLogger.run.append(gson.toJson(action));
	}
}
