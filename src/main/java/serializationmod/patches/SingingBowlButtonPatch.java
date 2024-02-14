package serializationmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.ui.buttons.SingingBowlButton;
import serializationmod.GameStateConverter;
import serializationmod.SerializationMod;

import java.util.TreeMap;

public class SingingBowlButtonPatch {
	@SpirePatch(
		clz= SingingBowlButton.class,
		method = "onClick"
	)
	public static class OnClickPatch {
		public static void Prefix(SingingBowlButton instance) {
			SerializationMod.run.append(GameStateConverter.getFloorState());

			TreeMap<String, Object> action = new TreeMap<>();
			action.put("_type", "action:bowl");

			Gson gson = new Gson();
			SerializationMod.run.append(gson.toJson(action));
		}
	}
}
