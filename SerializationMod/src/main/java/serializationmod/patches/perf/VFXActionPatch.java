package serializationmod.patches.perf;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.animations.VFXAction;

public class VFXActionPatch {
	@SpirePatch(
		clz= VFXAction.class,
		method="update"
	)
	public static class UpdatePatch {
		public static void Prefix(VFXAction instance) {
			// Speed up animations, e.g. attacks.
			// ReflectionHacks.setPrivateInherited(instance, VFXAction.class, "duration", 0.0F);
			// instance.duration = 0.0F;
		}
	}
}
