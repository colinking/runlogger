package runlogger.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.BattleStartEffect;

// Ported from:
// https://github.com/Skrelpoid/SuperFastMode/blob/531d29a944cc7f949f3d4c3925354a9c36db7a6e/src/main/java/skrelpoid/superfastmode/patches/DefaultDeltaPatches.java#L276
public class BattleStartEffectPatch {
	// Fixes Intents sometimes not being visible
	@SpirePatch(clz = com.megacrit.cardcrawl.vfx.combat.BattleStartEffect.class, method = "update")
	public static class IntentFix {
		@SpireInsertPatch(rloc = 10)
		public static void Insert(BattleStartEffect effect) {
			for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
				if (m.intent == AbstractMonster.Intent.DEBUG) {
					m.createIntent();
				}
			}
		}
	}
}
