package serializationmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.screens.options.ConfirmPopup;
import serializationmod.GameStateConverter;
import serializationmod.SerializationMod;

import java.util.TreeMap;

public class ConfirmPopupPatch {
	@SpirePatch(
		clz= ConfirmPopup.class,
		method = "yesButtonEffect"
	)
	public static class YesButtonEffectPatch {
		public static void Prefix(ConfirmPopup instance) {
			if (instance.type != ConfirmPopup.ConfirmType.ABANDON_MID_RUN) {
				return;
			}

			SerializationMod.run.append(GameStateConverter.getFloorState());

			TreeMap<String, Object> action = new TreeMap<>();
			action.put("_type", "action:abandon");

			Gson gson = new Gson();
			SerializationMod.run.append(gson.toJson(action));

			// 	TODO: print state once more once fully dead
		}
	}
}
