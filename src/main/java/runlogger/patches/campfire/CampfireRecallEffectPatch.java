package runlogger.patches.campfire;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.vfx.campfire.CampfireRecallEffect;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import runlogger.GameStateConverter;
import runlogger.RunLogger;

import java.util.ArrayList;
import java.util.TreeMap;

public class CampfireRecallEffectPatch {
	@SpirePatch(
		clz= CampfireRecallEffect.class,
		method="update"
	)
	public static class UpdatePatch {
		@SpireInsertPatch(
			locator = Locator.class
		)
		public static void Insert(CampfireRecallEffect instance) {
			RunLogger.run.append(GameStateConverter.getFloorState());

			TreeMap<String, Object> action = new TreeMap<>();
			action.put("_type", "action:recall");

			Gson gson = new Gson();
			RunLogger.run.append(gson.toJson(action));
		}

		private static class Locator extends SpireInsertLocator {
			public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
				Matcher matcher = new Matcher.MethodCallMatcher(SoundMaster.class, "play");
				return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), matcher);
			}
		}
	}
}
