package serializationmod.patches.perf;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class AbstractCardPatch {
	@SpirePatch(
		clz= AbstractCard.class,
		method="darken",
		paramtypez = {boolean.class}
	)
	public static class DarkenPatch {
		public static void Postfix(AbstractCard instance, boolean immediate) {
			// Makes discarding faster.
			// ReflectionHacks.setPrivate(instance, AbstractCard.class, "darkTimer", 0.0F);
		}
	}
}
