package runlogger.patches;

import com.badlogic.gdx.files.FileHandle;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.metrics.Metrics;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.screens.GameOverScreen;
import com.megacrit.cardcrawl.screens.VictoryScreen;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import runlogger.GameStateConverter;
import runlogger.RunLogger;

import java.io.File;
import java.util.ArrayList;

public class GameOverScreenPatch {
	@SpirePatch(clz = VictoryScreen.class, method = SpirePatch.CONSTRUCTOR)
	@SpirePatch(clz = DeathScreen.class, method = SpirePatch.CONSTRUCTOR)
	public static class ConstructorPatch {
		public static void Postfix(GameOverScreen instance) {
			// Print final time upon death/victory.
			RunLogger.run.append(GameStateConverter.getGameOverState(instance));
			RunLogger.run.flush();

			// This constructor also creates the ".run" file. Move our log to a corresponding path.
			//
			// Note that we can't store it directly in the "runs/" directory, otherwise STS detects it as an invalid run
			// file and automatically deletes it.
			String path = "runlogs" + File.separator + GameOverScreenPatch.MetricsPatch.lastRunDir + File.separator + GameOverScreenPatch.MetricsPatch.lastRunFileName + ".log";
			RunLogger.run.move(path);

			RunLogger.run = null;
		}
	}

	@SpirePatch(clz = Metrics.class, method = "gatherAllDataAndSave")
	public static class MetricsPatch {
		public static String lastRunDir;
		public static String lastRunFileName;

		@SpireInsertPatch(
			locator=Locator.class,
			localvars = {"file"}
		)
		public static void Insert(Metrics instance, FileHandle file) {
			lastRunDir = file.parent().name();
			lastRunFileName = file.name();
		}

		private static class Locator extends SpireInsertLocator {
			public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
				Matcher matcher = new Matcher.MethodCallMatcher(FileHandle.class, "writeString");
				return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), matcher);
			}
		}
	}
}
