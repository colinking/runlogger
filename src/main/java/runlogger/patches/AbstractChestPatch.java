package runlogger.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.rewards.chests.AbstractChest;
import runlogger.GameStateConverter;
import runlogger.RunLogger;

import java.util.TreeMap;

public class AbstractChestPatch {
	@SpirePatch(
		clz= AbstractChest.class,
		method = "open"
	)
	public static class OpenPatch {
		public static void Prefix(AbstractChest instance, boolean bossChest) {
			RunLogger.run.append(GameStateConverter.getFloorState());

			TreeMap<String, Object> action = new TreeMap<>();
			action.put("_type", "action:open_chest");

			Gson gson = new Gson();
			RunLogger.run.append(gson.toJson(action));
		}
	}
}
