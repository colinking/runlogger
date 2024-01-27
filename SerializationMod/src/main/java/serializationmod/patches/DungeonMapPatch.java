package serializationmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.map.DungeonMap;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import serializationmod.GameStateConverter;
import serializationmod.SerializationMod;

import java.util.ArrayList;
import java.util.TreeMap;

public class DungeonMapPatch {
	@SpirePatch(
		clz= DungeonMap.class,
		method="update"
	)
	public static class UpdatePatch {
		@SpireInsertPatch(
			locator= Locator.class
		)
		public static void Insert(DungeonMap instance) {
			SerializationMod.run.append(GameStateConverter.getFloorState());

			TreeMap<String, Object> action = new TreeMap<>();
			action.put("_type", "action:select_map");
			// TODO: are these coords correct?
			// TODO: map is missing symbol for boss...
			action.put("x", 3);
			action.put("y", 16);
			action.put("symbol", "B");

			Gson gson = new Gson();
			SerializationMod.run.append(gson.toJson(action));
		}

		private static class Locator extends SpireInsertLocator {
			public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
				Matcher matcher = new Matcher.NewExprMatcher(MonsterRoomBoss.class);
				return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), matcher);
			}
		}
	}
}
