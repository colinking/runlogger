package serializationmod.patches.campfire;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.vfx.campfire.CampfireSleepEffect;
import serializationmod.GameStateConverter;
import serializationmod.SerializationMod;

import java.util.TreeMap;

public class CampfireSleepEffectPatch {
	@SpirePatch(
		clz= CampfireSleepEffect.class,
		method="playSleepJingle"
	)
	public static class PlaySleepJinglePatch {
		public static void Prefix(CampfireSleepEffect instance) {
			SerializationMod.run.append(GameStateConverter.getFloorState());

			TreeMap<String, Object> action = new TreeMap<>();
			action.put("_type", "action:rest");

			Gson gson = new Gson();
			SerializationMod.run.append(gson.toJson(action));
		}
	}
}
