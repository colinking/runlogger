package runlogger.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.ui.buttons.SingingBowlButton;
import runlogger.GameStateConverter;
import runlogger.RunLogger;

import java.util.TreeMap;

public class SingingBowlButtonPatch {
	@SpirePatch(
		clz= SingingBowlButton.class,
		method = "onClick"
	)
	public static class OnClickPatch {
		public static void Prefix(SingingBowlButton instance) {
			RunLogger.run.append(GameStateConverter.getFloorState());

			TreeMap<String, Object> action = new TreeMap<>();
			action.put("_type", "action:bowl");

			Gson gson = new Gson();
			RunLogger.run.append(gson.toJson(action));
		}
	}
}
