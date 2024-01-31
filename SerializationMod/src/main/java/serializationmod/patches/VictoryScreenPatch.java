package serializationmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.screens.VictoryScreen;
import serializationmod.GameStateConverter;
import serializationmod.SerializationMod;

public class VictoryScreenPatch {
	@SpirePatch(clz = VictoryScreen.class, method = SpirePatch.CONSTRUCTOR)
	public static class ConstructorPatch {
		public static void Postfix(VictoryScreen instance) {
			// Print final time upon victory.
			SerializationMod.run.append(GameStateConverter.getGameOverState(instance));
		}
	}
}
