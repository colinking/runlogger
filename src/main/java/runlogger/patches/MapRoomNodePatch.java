package runlogger.patches;


import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.map.MapRoomNode;
import runlogger.GameStateConverter;
import runlogger.RunLogger;

import java.util.TreeMap;

public class MapRoomNodePatch {
	@SpirePatch(
		clz= MapRoomNode.class,
		method="playNodeSelectedSound"
	)
	public static class SelectNodePatch {
		public static void Prefix(MapRoomNode instance) {
			RunLogger.run.append(GameStateConverter.getFloorState());

			TreeMap<String, Object> action = new TreeMap<>();
			action.put("_type", "action:select_map");
			action.put("x", instance.x);
			action.put("y", instance.y);
			action.put("symbol", instance.getRoomSymbol(true));

			Gson gson = new Gson();
			RunLogger.run.append(gson.toJson(action));
		}
	}
}
