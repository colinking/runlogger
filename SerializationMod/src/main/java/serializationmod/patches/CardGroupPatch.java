package serializationmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.CardGroup;
import serializationmod.SerializationMod;

public class CardGroupPatch {
	@SpirePatch(
		clz= CardGroup.class,
		method = "refreshHandLayout"
	)
	public static class RefreshHandLayoutPatch {
		public static void Prefix(CardGroup instance) {
			SerializationMod.logger.info("Refreshing hand");
		}
	}
}
