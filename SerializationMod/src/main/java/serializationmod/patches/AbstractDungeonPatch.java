package serializationmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import serializationmod.GameStateConverter;
import serializationmod.SerializationMod;

import java.util.ArrayList;
import java.util.TreeMap;

public class AbstractDungeonPatch {
	@SpirePatch(
		clz=AbstractDungeon.class,
		method="closeCurrentScreen"
	)
	public static class CloseCurrentScreenPatch {
		public static void Prefix() {
			if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.GRID) {
				// TODO: distinguish between cancel and confirm??
				SerializationMod.run.append(GameStateConverter.getFloorState());

				TreeMap<String, Object> action = new TreeMap<>();
				action.put("_type", "action:select_cards");
				ArrayList<TreeMap<String, Object>> cards = new ArrayList<>();
				for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
					TreeMap<String, Object> c = new TreeMap<>();
					c.put("name", GameStateConverter.getCardName(card));
					c.put("index", AbstractDungeon.gridSelectScreen.targetGroup.group.indexOf(card));
					cards.add(c);
				}
				action.put("cards", cards);

				if (AbstractDungeon.gridSelectScreen.forUpgrade) {
					action.put("select_type", "smith");
				} else if (AbstractDungeon.gridSelectScreen.forPurge) {
					action.put("select_type", "remove");
				} else if (AbstractDungeon.gridSelectScreen.forTransform) {
					action.put("select_type", "transform");
				}
				// TODO: other select types...

				Gson gson = new Gson();
				SerializationMod.run.append(gson.toJson(action));
			}
		}
	}
}
