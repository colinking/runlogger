package serializationmod.patches.shop;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.metrics.MetricData;
import com.megacrit.cardcrawl.shop.ShopScreen;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import serializationmod.GameStateConverter;
import serializationmod.SerializationMod;

import java.util.ArrayList;
import java.util.TreeMap;

public class ShopScreenPatch {
	@SpirePatch(
		clz= ShopScreen.class,
		method="purchaseCard"
	)
	public static class PurchaseCardPatch {
		@SpireInsertPatch(
			locator = Locator.class
		)
		public static void Insert(ShopScreen instance, AbstractCard hoveredCard) {
			SerializationMod.run.append(GameStateConverter.getFloorState());

			TreeMap<String, Object> action = new TreeMap<>();
			action.put("_type", "action:buy");
			action.put("card", GameStateConverter.getCardName(hoveredCard));
			int index = instance.coloredCards.indexOf(hoveredCard);
			if (index == -1) {
				index = instance.colorlessCards.indexOf(hoveredCard) + instance.coloredCards.size();
			}
			action.put("card_index", index);

			Gson gson = new Gson();
			SerializationMod.run.append(gson.toJson(action));
		}

		private static class Locator extends SpireInsertLocator {
			public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
				Matcher matcher = new Matcher.MethodCallMatcher(MetricData.class, "addShopPurchaseData");
				return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), matcher);
			}
		}
	}
}
