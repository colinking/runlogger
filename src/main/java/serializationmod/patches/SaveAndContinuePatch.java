package serializationmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
import serializationmod.SerializationMod;

public class SaveAndContinuePatch {
	@SpirePatch(
		clz= SaveAndContinue.class,
		method="save"
	)
	public static class SavePatch {
		public static void Prefix() {
			// Flush the buffered state/action logs to disk to match the newly updated save file.
			SerializationMod.run.flush();
		}
	}
}
