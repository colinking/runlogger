package serializationmod.patches.events;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.events.shrines.GremlinWheelGame;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import serializationmod.GameStateConverter;
import serializationmod.SerializationMod;

import java.util.ArrayList;
import java.util.TreeMap;

public class GremlinWheelGamePatch {
	@SpirePatch(clz = GremlinWheelGame.class, method="update")
	public static class Patch {
		@SpireInsertPatch(locator= Locator.class)
		public static void Insert(GremlinWheelGame instance) {
			SerializationMod.run.append(GameStateConverter.getFloorState());

			TreeMap<String, Object> action = new TreeMap<>();
			action.put("_type", "action:spin");

			Gson gson = new Gson();
			SerializationMod.run.append(gson.toJson(action));
		}

		private static class Locator extends SpireInsertLocator {
			public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
				Matcher matcher = new Matcher.FieldAccessMatcher(GremlinWheelGame.class, "buttonPressed");
				int[] lines = LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<Matcher>(), matcher);
				// Select the second field access. This detects when buttonPressed is set to "true".
				return new int[]{lines[1]};
			}
		}
	}
}
