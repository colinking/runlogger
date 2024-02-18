package runlogger.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.rewards.chests.BossChest;
import runlogger.GameStateConverter;
import runlogger.RunLogger;

import java.util.TreeMap;

public class BossChestPatch {
	@SpirePatch(
		clz= BossChest.class,
		method = "open"
	)
	public static class OpenPatch {
		public static void Prefix(BossChest instance, boolean bossChest) {
			RunLogger.run.append(GameStateConverter.getFloorState());

			TreeMap<String, Object> action = new TreeMap<>();
			action.put("_type", "action:open_chest");

			Gson gson = new Gson();
			RunLogger.run.append(gson.toJson(action));
		}
	}
}
