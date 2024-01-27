package serializationmod.patches.perf;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class AbstractGameActionPatch {
	@SpirePatch(
		clz= AbstractGameAction.class,
		method="tickDuration"
	)
	public static class UpdatePatch {
		public static void Prefix(AbstractGameAction instance) {
			// Speed up animations, e.g. attacks.
			// float duration = ReflectionHacks.getPrivate(instance, AbstractGameAction.class, "duration");
			// ReflectionHacks.setPrivate(instance, AbstractGameAction.class, "duration", 0.0F);
			// float durationAfter = ReflectionHacks.getPrivate(instance, AbstractGameAction.class, "duration");
			// SerializationMod.logger.info("colin: updated duration" + instance.getClass().getName() + ", before " + Float.toString(duration) + ", after " + Float.toString(durationAfter));

			// instance.duration = 0.0F;
		}
	}
}
