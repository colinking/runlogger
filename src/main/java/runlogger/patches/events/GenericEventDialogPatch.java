package runlogger.patches.events;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import runlogger.GameStateConverter;
import runlogger.RunLogger;

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
		RunLogger.run.append(GameStateConverter.getFloorState());

		TreeMap<String, Object> action = new TreeMap<>();
		action.put("_type", "action:select_dialog");
		action.put("index", i);

		Gson gson = new Gson();
		RunLogger.run.append(gson.toJson(action));
	}

	private static class Locator extends SpireInsertLocator {
		public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
			Matcher matcher = new Matcher.FieldAccessMatcher(GenericEventDialog.class, "selectedOption");
			return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), matcher);
		}
	}

}
