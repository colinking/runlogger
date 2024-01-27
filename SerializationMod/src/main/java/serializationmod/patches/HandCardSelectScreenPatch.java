package serializationmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;
import serializationmod.GameStateConverter;
import serializationmod.SerializationMod;

public class HandCardSelectScreenPatch {
	@SpirePatch(
		clz= HandCardSelectScreen.class,
		method="open",
		paramtypez = {String.class, int.class, boolean.class, boolean.class}
	)
	public static class OpenPatch {
		public static void Prefix(HandCardSelectScreen instance, String msg, int amount, boolean anyNumber, boolean canPickZero) {
			// We serialize state separately from serializing the action because selecting cards
			// removes them from the user's hand. Therefore, we must serialize the state before
			// they start selecting cards.
			SerializationMod.run.append(GameStateConverter.getFloorState());
		}
	}
}
