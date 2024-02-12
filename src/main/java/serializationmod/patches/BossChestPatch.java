package serializationmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.rewards.chests.BossChest;
import serializationmod.GameStateConverter;
import serializationmod.SerializationMod;

import java.util.TreeMap;

public class BossChestPatch {
	@SpirePatch(
		clz= BossChest.class,
		method = "open"
	)
	public static class OpenPatch {
		public static void Prefix(BossChest instance, boolean bossChest) {
			SerializationMod.run.append(GameStateConverter.getFloorState());

			TreeMap<String, Object> action = new TreeMap<>();
			action.put("_type", "action:open_chest");

			Gson gson = new Gson();
			SerializationMod.run.append(gson.toJson(action));
		}
	}
}
