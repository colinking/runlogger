package runlogger.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.Exordium;
import runlogger.GameStateConverter;
import runlogger.RunLogger;

import java.util.ArrayList;

public class ExordiumPatch {
	@SpirePatch(
		clz= Exordium.class,
		method=SpirePatch.CONSTRUCTOR,
		paramtypez={
			AbstractPlayer.class,
			ArrayList.class,
		}
	)
	public static class ConstructorPatch {
		public static void Postfix(Exordium instance, AbstractPlayer p, ArrayList emptyList) {
			RunLogger.run.append(GameStateConverter.getRunState());
			RunLogger.run.flush();
		}
	}
}
