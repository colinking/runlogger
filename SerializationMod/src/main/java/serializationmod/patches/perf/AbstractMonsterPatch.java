package serializationmod.patches.perf;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class AbstractMonsterPatch {
	@SpirePatch(
		clz= AbstractMonster.class,
		method="die",
		paramtypez = {boolean.class}
	)
	public static class DiePatch {
		public static void Postfix(AbstractMonster instance, boolean triggerRelics) {
			// Speed up enemy deaths.
			// instance.deathTimer = 0.05F;
		}
	}
}
