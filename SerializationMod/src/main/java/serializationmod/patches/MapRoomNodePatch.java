package serializationmod.patches;


import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.map.MapRoomNode;
import serializationmod.GameStateConverter;
import serializationmod.SerializationMod;

import java.util.TreeMap;

public class MapRoomNodePatch {
	@SpirePatch(
		clz= MapRoomNode.class,
		method="playNodeSelectedSound"
	)
	public static class SelectNodePatch {
		public static void Prefix(MapRoomNode instance) {
			SerializationMod.run.append(GameStateConverter.getFloorState());

			TreeMap<String, Object> action = new TreeMap<>();
			action.put("_type", "action:select_map");
			action.put("x", instance.x);
			action.put("y", instance.y);
			action.put("symbol", instance.getRoomSymbol(true));

			Gson gson = new Gson();
			SerializationMod.run.append(gson.toJson(action));
		}
	}
}
