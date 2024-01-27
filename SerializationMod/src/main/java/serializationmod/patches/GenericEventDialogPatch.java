package serializationmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import serializationmod.GameStateConverter;
import serializationmod.SerializationMod;

import java.util.ArrayList;
import java.util.TreeMap;

@SpirePatch(
	clz= GenericEventDialog.class,
	method="update"
)
public class GenericEventDialogPatch {
	@SpireInsertPatch(
		locator=Locator.class,
		localvars={"i"}
	)
	public static void Insert(GenericEventDialog instance, int i) {
		SerializationMod.run.append(GameStateConverter.getFloorState());

		TreeMap<String, Object> action = new TreeMap<>();
		action.put("_type", "action:select_dialog");
		action.put("index", i);

		Gson gson = new Gson();
		SerializationMod.run.append(gson.toJson(action));
	}

	private static class Locator extends SpireInsertLocator {
		public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
			Matcher matcher = new Matcher.FieldAccessMatcher(GenericEventDialog.class, "selectedOption");
			return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), matcher);
		}
	}

}
