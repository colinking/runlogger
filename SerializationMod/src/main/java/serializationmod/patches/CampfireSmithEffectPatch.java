package serializationmod;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.metrics.MetricData;
import com.megacrit.cardcrawl.vfx.campfire.CampfireSmithEffect;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.TreeMap;

public class CampfireSmithEffectPatch {
	@SpirePatch(
		clz= CampfireSmithEffect.class,
		method="update"
	)
	public static class UpdatePatch {
		@SpireInsertPatch(
			locator = Locator.class,
			localvars = {"c"}
		)
		public static void Insert(CampfireSmithEffect instance, AbstractCard c) {
			SerializationMod.run.append(GameStateConverter.getFloorState());

			TreeMap<String, Object> action = new TreeMap<>();
			action.put("_type", "action:smith");
			action.put("card", GameStateConverter.getCardName(c));
			action.put("card_index", AbstractDungeon.gridSelectScreen.targetGroup.group.indexOf(c));

			Gson gson = new Gson();
			SerializationMod.run.append(gson.toJson(action));
		}

		private static class Locator extends SpireInsertLocator {
			public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
				Matcher matcher = new Matcher.MethodCallMatcher(MetricData.class, "addCampfireChoiceData");
				return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), matcher);
			}
		}
	}
}
