package serializationmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.screens.DeathScreen;
import serializationmod.GameStateConverter;
import serializationmod.SerializationMod;

public class DeathScreenPatch {
	@SpirePatch(clz = DeathScreen.class, method = SpirePatch.CONSTRUCTOR)
	public static class ConstructorPatch {
		public static void Postfix(DeathScreen instance) {
			// Print final time upon death.
			SerializationMod.run.append(GameStateConverter.getGameOverState(instance));
		}
	}
}
