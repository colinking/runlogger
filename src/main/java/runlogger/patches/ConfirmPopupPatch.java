package runlogger.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.screens.options.ConfirmPopup;
import runlogger.GameStateConverter;
import runlogger.RunLogger;

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

			RunLogger.run.append(GameStateConverter.getFloorState());

			TreeMap<String, Object> action = new TreeMap<>();
			action.put("_type", "action:abandon");

			Gson gson = new Gson();
			RunLogger.run.append(gson.toJson(action));
		}
	}
}
