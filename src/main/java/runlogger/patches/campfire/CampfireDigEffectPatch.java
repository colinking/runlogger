package runlogger.patches.campfire;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.vfx.campfire.CampfireDigEffect;
import runlogger.GameStateConverter;
import runlogger.RunLogger;

import java.util.TreeMap;

public class CampfireDigEffectPatch {
	@SpirePatch(
		clz= CampfireDigEffect.class,
		method=SpirePatch.CONSTRUCTOR
	)
	public static class ConstructorPatch {
		public static void Postfix(CampfireDigEffect instance) {
			RunLogger.run.append(GameStateConverter.getFloorState());

			TreeMap<String, Object> action = new TreeMap<>();
			action.put("_type", "action:dig");

			Gson gson = new Gson();
			RunLogger.run.append(gson.toJson(action));
		}
	}
}
