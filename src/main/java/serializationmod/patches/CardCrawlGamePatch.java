package serializationmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import serializationmod.GameStateConverter;
import serializationmod.SerializationMod;

public class CardCrawlGamePatch {
	@SpirePatch(
		clz= CardCrawlGame.class,
		method="getDungeon",
		paramtypez={String.class, AbstractPlayer.class}
	)
	public static class GetDungeonPatch {
		public static void Postfix(CardCrawlGame instance, String key, AbstractPlayer p) {
			SerializationMod.run.append(GameStateConverter.getActState());
		}
	}
}
