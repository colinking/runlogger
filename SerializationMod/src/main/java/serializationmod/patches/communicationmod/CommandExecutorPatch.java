package serializationmod.patches.communicationmod;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import serializationmod.patches.PotionPopUpPatch;

import java.util.ArrayList;

/**
 * CommunicationMod calls the abstract `AbstractPotion.use()` method directly, so we can't
 * detect that via patching without manually patching every possible potion. Therefore, to
 * detect potion usage via CommunicationMod, we patch CommunicationMod directly.
 *
 * Similarly, we need to patch CommunicationMod to detect potion discarding.
 */
@SuppressWarnings("unused")
public class CommandExecutorPatch {
	@SpirePatch(
		cls="communicationmod.CommandExecutor",
		method="executePotionCommand",
		requiredModId = "CommunicationMod",
		optional = true
	)
	public static class ExecutePotionCommandUseTargetedPatch {
		@SpireInsertPatch(locator= Locator.class, localvars = {"selectedPotion", "target_monster"})
		public static void Insert(Object instance, AbstractPotion selectedPotion, AbstractMonster target_monster) {
			PotionPopUpPatch.recordPotionUsage(selectedPotion, target_monster);
		}

		private static class Locator extends SpireInsertLocator {
			public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
				Matcher matcher = new Matcher.MethodCallMatcher(AbstractPotion.class, "use");
				int[] lines = LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
				// The first potion.use() call is for targeted usage.
				return new int[]{lines[0]};
			}
		}
	}

	@SpirePatch(
		cls="communicationmod.CommandExecutor",
		method="executePotionCommand",
		requiredModId = "CommunicationMod",
		optional = true
	)
	public static class ExecutePotionCommandUseUntargetedPatch {
		@SpireInsertPatch(locator= Locator.class, localvars = {"selectedPotion"})
		public static void Insert(Object instance, AbstractPotion selectedPotion) {
			PotionPopUpPatch.recordPotionUsage(selectedPotion, null);
		}

		private static class Locator extends SpireInsertLocator {
			public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
				Matcher matcher = new Matcher.MethodCallMatcher(AbstractPotion.class, "use");
				int[] lines = LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
				// The second potion.use() call is for untargeted usage.
				return new int[]{lines[1]};
			}
		}
	}

	@SpirePatch(
		cls="communicationmod.CommandExecutor",
		method="executePotionCommand",
		requiredModId = "CommunicationMod",
		optional = true
	)
	public static class ExecutePotionCommandDiscardPatch {
		@SpireInsertPatch(locator= Locator.class, localvars = {"use", "selectedPotion"})
		public static void Insert(Object instance, boolean use, AbstractPotion selectedPotion) {
			if (!use) {
				PotionPopUpPatch.recordPotionDiscard(selectedPotion);
			}
		}

		private static class Locator extends SpireInsertLocator {
			public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
				Matcher matcher = new Matcher.MethodCallMatcher(TopPanel.class, "destroyPotion");
				return LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<>(), matcher);
			}
		}
	}
}
