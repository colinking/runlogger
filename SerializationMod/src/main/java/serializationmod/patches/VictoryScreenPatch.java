package serializationmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.screens.VictoryScreen;
import serializationmod.GameStateConverter;
import serializationmod.SerializationMod;

public class VictoryScreenPatch {
	@SpirePatch(clz = VictoryScreen.class, method = "updateAscensionAndBetaArtProgress")
	public static class UpdateAscensionAndBetaArtProgressPatch {
		public static void Prefix(VictoryScreen instance) {
			// Print final time upon victory.
			SerializationMod.run.append(GameStateConverter.getGameOverState());
		}
	}
}
