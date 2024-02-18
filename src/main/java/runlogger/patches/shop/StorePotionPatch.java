package runlogger.patches.shop;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StorePotion;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import runlogger.GameStateConverter;
import runlogger.RunLogger;

import java.util.ArrayList;
import java.util.TreeMap;

public class StorePotionPatch {
	@SpirePatch(
		clz= StorePotion.class,
		method="purchasePotion"
	)
	public static class PurchaseRelicPatch {
		@SpireInsertPatch(
			locator= Locator.class
		)
		public static void Insert(StorePotion instance) {
			RunLogger.run.append(GameStateConverter.getFloorState());

			TreeMap<String, Object> action = new TreeMap<>();
			action.put("_type", "action:buy");
			action.put("potion", instance.potion.name);
			ArrayList<StorePotion> potions = ReflectionHacks.getPrivate(AbstractDungeon.shopScreen, ShopScreen.class, "potions");
			action.put("potion_index", potions.indexOf(instance));

			Gson gson = new Gson();
			RunLogger.run.append(gson.toJson(action));
		}

		private static class Locator extends SpireInsertLocator {
			public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
				Matcher matcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "loseGold");
				return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), matcher);
			}
		}
	}
}
