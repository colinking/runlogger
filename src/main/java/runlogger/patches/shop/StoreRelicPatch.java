package runlogger.patches.shop;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.shop.StoreRelic;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import runlogger.GameStateConverter;
import runlogger.RunLogger;

import java.util.ArrayList;
import java.util.TreeMap;

public class StoreRelicPatch {
	@SpirePatch(
		clz= StoreRelic.class,
		method="purchaseRelic"
	)
	public static class PurchaseRelicPatch {
		@SpireInsertPatch(
			locator= Locator.class
		)
		public static void Insert(StoreRelic instance) {
			RunLogger.run.append(GameStateConverter.getFloorState());

			TreeMap<String, Object> action = new TreeMap<>();
			action.put("_type", "action:buy");
			action.put("relic", instance.relic.name);
			ShopRoom room = (ShopRoom) AbstractDungeon.getCurrRoom();
			action.put("relic_index", room.relics.indexOf(instance.relic));

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
