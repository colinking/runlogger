package runlogger.patches.campfire;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.vfx.campfire.CampfireSleepEffect;
import runlogger.GameStateConverter;
import runlogger.RunLogger;

import java.util.TreeMap;

public class CampfireSleepEffectPatch {
	@SpirePatch(
		clz= CampfireSleepEffect.class,
		method="playSleepJingle"
	)
	public static class PlaySleepJinglePatch {
		public static void Prefix(CampfireSleepEffect instance) {
			RunLogger.run.append(GameStateConverter.getFloorState());

			TreeMap<String, Object> action = new TreeMap<>();
			action.put("_type", "action:rest");

			Gson gson = new Gson();
			RunLogger.run.append(gson.toJson(action));
		}
	}
}
