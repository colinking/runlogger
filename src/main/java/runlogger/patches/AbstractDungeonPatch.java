package runlogger.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import runlogger.RunFile;
import runlogger.RunLogger;

import java.util.ArrayList;

public class AbstractDungeonPatch {
	@SpirePatch(
		clz= AbstractDungeon.class,
		method=SpirePatch.CONSTRUCTOR,
		paramtypez={
			String.class,
			String.class,
			AbstractPlayer.class,
			ArrayList.class
		}
	)
	public static class ConstructorNewPatch {
		public static void Prefix(AbstractDungeon instance, String name, String levelId, AbstractPlayer p, ArrayList<String> newSpecialOneTimeEventList) {
			if (levelId.equals(Exordium.ID) && AbstractDungeon.floorNum == 0) {
				RunLogger.run = new RunFile(p.chosenClass, false);
			}
		}
	}

	@SpirePatch(
		clz=AbstractDungeon.class,
		method=SpirePatch.CONSTRUCTOR,
		paramtypez={
			String.class,
			AbstractPlayer.class,
			SaveFile.class
		}
	)
	public static class ConstructorContinuePatch {
		public static void Prefix(AbstractDungeon instance, String name, AbstractPlayer p, SaveFile saveFile) {
			RunLogger.run = new RunFile(p.chosenClass, true);
		}
	}
}
