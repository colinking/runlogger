package serializationmod.patches.perf;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

public class ShowCardAndObtainEffectPatch {
	@SpirePatch(clz = ShowCardAndObtainEffect.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, float.class, float.class, boolean.class})
	public static class ConstructorPatch {
		public static void Postfix(ShowCardAndObtainEffect instance, AbstractCard card, float x, float y, boolean convergeCards) {
			// Speed up how fast cards are acquired. This needs to be fast enough to complete before
			// we perform the next action (else the cards are not acquired in time -- or at all!).
			instance.duration = 0.01F;
		}
	}
}
