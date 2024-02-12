package serializationmod.patches.campfire;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.vfx.campfire.CampfireLiftEffect;
import serializationmod.GameStateConverter;
import serializationmod.SerializationMod;

import java.util.TreeMap;

public class CampfireLiftEffectPatch {
	@SpirePatch(
		clz= CampfireLiftEffect.class,
		method=SpirePatch.CONSTRUCTOR
	)
	public static class ConstructorPatch {
		public static void Postfix(CampfireLiftEffect instance) {
			SerializationMod.run.append(GameStateConverter.getFloorState());

			TreeMap<String, Object> action = new TreeMap<>();
			action.put("_type", "action:lift");

			Gson gson = new Gson();
			SerializationMod.run.append(gson.toJson(action));
		}
	}
}
