package serializationmod.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.ui.panels.PotionPopUp;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import serializationmod.GameStateConverter;
import serializationmod.SerializationMod;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Note that we detect using/discarding potions via CommunicationMod in CommandExecutorPatch.
 */
public class PotionPopUpPatch {
	@SpirePatch(
		clz= PotionPopUp.class,
		method="updateInput"
	)
	public static class UpdateInputUsePatch {
		@SpireInsertPatch(locator=Locator.class)
		public static void Insert(PotionPopUp instance) {
			// An _untargeted_ potion is being used.
			AbstractPotion potion = ReflectionHacks.getPrivate(instance, PotionPopUp.class, "potion");
			PotionPopUpPatch.recordPotionUsage(potion, null);
		}

		private static class Locator extends SpireInsertLocator {
			public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
				Matcher matcher = new Matcher.MethodCallMatcher(AbstractPotion.class, "use");
				return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
			}
		}
	}

	@SpirePatch(
		clz= PotionPopUp.class,
		method="updateInput"
	)
	public static class UpdateInputDiscardPatch {
		@SpireInsertPatch(locator=Locator.class)
		public static void Insert(PotionPopUp instance) {
			// A potion is being discarded.
			AbstractPotion potion = ReflectionHacks.getPrivate(instance, PotionPopUp.class, "potion");
			PotionPopUpPatch.recordPotionDiscard(potion);
		}

		private static class Locator extends SpireInsertLocator {
			public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
				Matcher matcher = new Matcher.MethodCallMatcher(TopPanel.class, "destroyPotion");
				// This is called twice; once when using and again when discarding. Patch when discarding.
				int[] lines = LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
				return new int[]{lines[1]};
			}
		}
	}

	@SpirePatch(
		clz= PotionPopUp.class,
		method="updateTargetMode"
	)
	public static class UpdateTargetModeUsePatch {
		@SpireInsertPatch(locator=Locator.class)
		public static void Insert(PotionPopUp instance) {
			// A _targeted_ potion is being used.
			AbstractPotion potion = ReflectionHacks.getPrivate(instance, PotionPopUp.class, "potion");
			AbstractMonster monster = ReflectionHacks.getPrivate(instance, PotionPopUp.class, "hoveredMonster");
			PotionPopUpPatch.recordPotionUsage(potion, monster);
		}

		private static class Locator extends SpireInsertLocator {
			public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
				Matcher matcher = new Matcher.MethodCallMatcher(AbstractPotion.class, "use");
				return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
			}
		}
	}

	public static void recordPotionUsage(AbstractPotion potion, AbstractMonster monster) {
		SerializationMod.run.append(GameStateConverter.getFloorState());

		TreeMap<String, Object> action = new TreeMap<>();
		action.put("_type", "action:use_potion");
		action.put("potion", potion.ID);
		action.put("potion_index", AbstractDungeon.player.potions.indexOf(potion));

		if (monster != null) {
			action.put("monster", monster.id);
			action.put("monster_index",  AbstractDungeon.getCurrRoom().monsters.monsters.indexOf(monster));
		}

		Gson gson = new Gson();
		SerializationMod.run.append(gson.toJson(action));
	}

	public static void recordPotionDiscard(AbstractPotion potion) {
		SerializationMod.run.append(GameStateConverter.getFloorState());

		TreeMap<String, Object> action = new TreeMap<>();
		action.put("_type", "action:discard_potion");
		action.put("potion", potion.ID);
		action.put("potion_index", AbstractDungeon.player.potions.indexOf(potion));

		Gson gson = new Gson();
		SerializationMod.run.append(gson.toJson(action));
	}
}
