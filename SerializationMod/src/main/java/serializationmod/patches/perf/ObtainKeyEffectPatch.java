package serializationmod.patches.perf;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.vfx.ObtainKeyEffect;

public class ObtainKeyEffectPatch {
	@SpirePatch(clz = ObtainKeyEffect.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {ObtainKeyEffect.KeyColor.class})
	public static class ConstructorPatch {
		public static void Postfix(ObtainKeyEffect instance, ObtainKeyEffect.KeyColor keyColor) {
			// Speed up obtaining keys so that they are visible in the next state update.
			instance.duration = 0.01F;
			instance.startingDuration = 0.01F;
		}
	}
}
