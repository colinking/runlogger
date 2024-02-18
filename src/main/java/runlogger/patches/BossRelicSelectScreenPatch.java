package runlogger.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.select.BossRelicSelectScreen;
import runlogger.GameStateConverter;
import runlogger.RunLogger;

import java.util.TreeMap;

public class BossRelicSelectScreenPatch {
	@SpirePatch(
		clz= BossRelicSelectScreen.class,
		method="relicObtainLogic"
	)
	public static class RelicObtainLogicPatch {
		public static void Prefix(BossRelicSelectScreen instance, AbstractRelic r) {
			RunLogger.run.append(GameStateConverter.getFloorState());

			TreeMap<String, Object> action = new TreeMap<>();
			action.put("_type", "action:select_boss_relic");
			action.put("relic", r.name);
			action.put("relic_index", AbstractDungeon.bossRelicScreen.relics.indexOf(r));

			Gson gson = new Gson();
			RunLogger.run.append(gson.toJson(action));
		}
	}

	@SpirePatch(
		clz= BossRelicSelectScreen.class,
		method="relicSkipLogic"
	)
	public static class RelicSkipLogicPatch {
		public static void Prefix(BossRelicSelectScreen instance) {
			RunLogger.run.append(GameStateConverter.getFloorState());

			TreeMap<String, Object> action = new TreeMap<>();
			action.put("_type", "action:skip");

			Gson gson = new Gson();
			RunLogger.run.append(gson.toJson(action));
		}
	}
}
