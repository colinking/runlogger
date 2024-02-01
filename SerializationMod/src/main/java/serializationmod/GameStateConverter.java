package serializationmod;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.IronWave;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.shrines.GremlinMatchGame;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.SeedHelper;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.neow.NeowRoom;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.RunicDome;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.*;
import com.megacrit.cardcrawl.screens.GameOverScreen;
import com.megacrit.cardcrawl.screens.GameOverStat;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StorePotion;
import com.megacrit.cardcrawl.shop.StoreRelic;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.ObtainKeyEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import serializationmod.patches.events.GremlinMatchGamePatch;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;

public class GameStateConverter {
	/**
	 * State that does not change during a run.
	 */
	public static String getRunState() {
		TreeMap<String, Object> state = new TreeMap<>();

		state.put("_type", "state:run");

		state.put("seed", SeedHelper.getString(Settings.seed));
		state.put("class", AbstractDungeon.player.chosenClass.name());

		TreeMap<String, Object> unlocks = new TreeMap<>();
		for (AbstractPlayer.PlayerClass cls : AbstractPlayer.PlayerClass.values()) {
			unlocks.put(cls.name(), UnlockTracker.getUnlockLevel(cls));
		}
		unlocks.put("final_act", Settings.isFinalActAvailable);
		int maxAscensionLevel = 0;
		if (AbstractDungeon.player.getPrefs().getInteger("WIN_COUNT", 0) > 0) {
			maxAscensionLevel = AbstractDungeon.player.getPrefs().getInteger("ASCENSION_LEVEL", 1);
		}
		unlocks.put("ascension", maxAscensionLevel);
		state.put("unlocks", unlocks);

		state.put("ascension", AbstractDungeon.ascensionLevel);

		TreeMap<String, Boolean> bossesSeen = new TreeMap<>();
		for (String boss : UnlockTracker.bossSeenPref.get().keySet()) {
			bossesSeen.put(boss, UnlockTracker.isBossSeen(boss));
		}

		state.put("bosses_seen", bossesSeen);

		TreeMap<String, String> versions = new TreeMap<>();
		versions.put("java", System.getProperty("java.version"));
		String stsVersion = Loader.STS_VERSION;
		if (Loader.STS_BETA) {
			stsVersion += " BETA";
		}
		versions.put("sts", stsVersion);
		state.put("versions", versions);

		TreeMap<String, String> mods = new TreeMap<>();
		for (ModInfo info : Loader.MODINFOS) {
			String version = null;
			if (info.ModVersion != null) {
				version = info.ModVersion.toString();
			}
			mods.put(info.getIDName(), version);
		}
		state.put("mods", mods);

		state.put("is_mini_blessing", GameStateConverter.isMiniBlessing());

		if (Settings.isDemo) {
			state.put("demo", true);
		}
		if (Settings.isTrial) {
			state.put("trial", true);
		}

		state.put("note_for_yourself_card", getNoteForYourselfCard());

		Gson gson = new Gson();
		return gson.toJson(state);
	}

	/**
	 * Returns true if the current run is a "mini blessing" (the character did not
	 * reach the act 1 boss in the previous run).
	 */
	private static boolean isMiniBlessing() {
		NeowRoom room = ((NeowRoom) AbstractDungeon.currMapNode.room);
		NeowEvent event = (NeowEvent) room.event;
		int bossCount = ReflectionHacks.getPrivate(event, NeowEvent.class, "bossCount");
		return bossCount == 0;
	}

	/**
	 * State that does not change during an act.
	 */
	public static String getActState() {
		TreeMap<String, Object> state = new TreeMap<>();

		state.put("_type", "state:act");

		state.put("act", AbstractDungeon.actNum);
		state.put("act_boss", AbstractDungeon.bossKey);
		state.put("map", convertMapToJson());

		Gson gson = new Gson();
		return gson.toJson(state);
	}

	public static String getGameOverState(GameOverScreen screen) {
		TreeMap<String, Object> state = new TreeMap<>();

		state.put("_type", "state:game_over");

		state.put("total_score", ReflectionHacks.getPrivate(screen, GameOverScreen.class, "score"));
		state.put("victory", GameOverScreen.isVictory);

		ArrayList<GameOverStat> stats = ReflectionHacks.getPrivate(screen, GameOverScreen.class, "stats");
		TreeMap<String, Object> score = new TreeMap<>();
		for (GameOverStat stat : stats) {
			if (stat.label != null && stat.value != null && !stat.label.equals("Score")) {
				score.put(stat.label, stat.value);
			}
		}
		state.put("score", score);

		Gson gson = new Gson();
		return gson.toJson(state);
	}

	/**
	 * All other state.
	 */
	public static String getFloorState() {
		TreeMap<String, Object> state = new TreeMap<>();

		state.put("_type", "state:floor");

		state.put("floor", AbstractDungeon.floorNum);
		String roomType = AbstractDungeon.getCurrRoom().getClass().getSimpleName();
		state.put("room_type", roomType);
		if (roomType.equals("EventRoom")) {
			AbstractEvent event = AbstractDungeon.getCurrRoom().event;
			state.put("event_name", ReflectionHacks.getPrivateStatic(event.getClass(), "NAME"));
		}

		state.put("hp_current", AbstractDungeon.player.currentHealth);
		state.put("hp_max", AbstractDungeon.player.maxHealth);

		state.put("gold", AbstractDungeon.player.gold);

		ArrayList<Object> relics = new ArrayList<>();
		for (AbstractRelic relic : AbstractDungeon.player.relics) {
			relics.add(convertRelicToJson(relic));
		}
		state.put("relics", relics);

		ArrayList<Object> deck = new ArrayList<>();
		for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
			deck.add(getCardName(card));
		}
		for (AbstractGameEffect effect : AbstractDungeon.effectList) {
			// Check for cards that are animating into the deck. Record them as already in the deck.
			if (effect instanceof ShowCardAndObtainEffect && !effect.isDone) {
				AbstractCard card = ReflectionHacks.getPrivate(effect, ShowCardAndObtainEffect.class, "card");
				deck.add(getCardName(card));
			}
		}
		state.put("deck", deck);

		ArrayList<Object> potions = new ArrayList<>();
		for (AbstractPotion potion : AbstractDungeon.player.potions) {
			if (potion.ID.equals("Potion Slot")) {
				potions.add(null);
			} else {
				potions.add(convertPotionToJson(potion));
			}
		}
		state.put("potions", potions);

		if (AbstractDungeon.getCurrRoom().phase.equals(AbstractRoom.RoomPhase.COMBAT)) {
			state.put("combat_state", getCombatState());
		}
		state.put("screen_state", getScreenState());

		if (AbstractDungeon.getCurrRoom().getClass() == ShopRoom.class) {
			state.put("shop", getShopState());
		}

		TreeMap<String, Boolean> keys = new TreeMap<>();
		if (Settings.hasRubyKey) {
			keys.put("ruby", true);
		}
		if (Settings.hasEmeraldKey) {
			keys.put("emerald", true);
		}
		if (Settings.hasSapphireKey) {
			keys.put("sapphire", true);
		}
		for (AbstractGameEffect effect : AbstractDungeon.effectList) {
			// Check for keys that are animating into an obtained state. Record them as already obtained.
			if (effect instanceof ObtainKeyEffect && !effect.isDone) {
				ObtainKeyEffect.KeyColor keyColor = ReflectionHacks.getPrivate(effect, ObtainKeyEffect.class, "keyColor");
				if (keyColor == ObtainKeyEffect.KeyColor.RED) {
					keys.put("ruby", true);
				}
				if (keyColor == ObtainKeyEffect.KeyColor.GREEN) {
					keys.put("emerald", true);
				}
				if (keyColor == ObtainKeyEffect.KeyColor.BLUE) {
					keys.put("sapphire", true);
				}
			}
		}
		if (!keys.isEmpty()) {
			state.put("keys", keys);
		}

		if (!AbstractDungeon.combatRewardScreen.rewards.isEmpty()) {
			state.put("rewards", getRewardState());
		}

		state.put("seeds", getSeedState());

		Gson gson = new Gson();
		return gson.toJson(state);
	}

	// Useful for debugging
	private static HashMap<String, Object> getSeedState() {
		HashMap<String, Object> state = new HashMap<>();
		state.put("monster", AbstractDungeon.monsterRng.counter);
		state.put("map", AbstractDungeon.mapRng.counter);
		state.put("event", AbstractDungeon.eventRng.counter);
		state.put("card", AbstractDungeon.cardRng.counter);
		state.put("treasure", AbstractDungeon.treasureRng.counter);
		state.put("relic", AbstractDungeon.relicRng.counter);
		state.put("potion", AbstractDungeon.potionRng.counter);
		state.put("monster_hp", AbstractDungeon.monsterHpRng.counter);
		state.put("ai", AbstractDungeon.aiRng.counter);
		state.put("shuffle", AbstractDungeon.shuffleRng.counter);
		state.put("card_random", AbstractDungeon.cardRandomRng.counter);
		state.put("misc", AbstractDungeon.miscRng.counter);
		return state;
	}

	private static TreeMap<String, Object> getRoomState() {
		AbstractRoom currentRoom = AbstractDungeon.getCurrRoom();
		TreeMap<String, Object> state = new TreeMap<>();
		if (currentRoom instanceof TreasureRoom) {
			state.put("chest_type", ((TreasureRoom) currentRoom).chest.getClass().getSimpleName());
			state.put("chest_open", ((TreasureRoom) currentRoom).chest.isOpen);
		} else if (currentRoom instanceof TreasureRoomBoss) {
			state.put("chest_type", ((TreasureRoomBoss) currentRoom).chest.getClass().getSimpleName());
			state.put("chest_open", ((TreasureRoomBoss) currentRoom).chest.isOpen);
		} else if (currentRoom instanceof RestRoom) {
			state.put("has_rested", currentRoom.phase == AbstractRoom.RoomPhase.COMPLETE);
			state.put("rest_options", ChoiceScreenUtils.getRestRoomChoices());
		}
		return state;
	}

	/**
	 * This method removes the special text formatting characters found in the game.
	 * These extra formatting characters are turned into things like colored or wiggly text in game, but
	 * we would like to report the text without dealing with these characters.
	 *
	 * @param text The text for which the formatting should be removed
	 * @return The input text, with the formatting characters removed
	 */
	private static String removeTextFormatting(String text) {
		text = text.replaceAll("~|@(\\S+)~|@", "$1");
		return text.replaceAll("#.|NL", "");
	}

	private static TreeMap<String, Object> getEventState() {
		TreeMap<String, Object> state = new TreeMap<>();
		ArrayList<Object> options = new ArrayList<>();
		ChoiceScreenUtils.EventDialogType eventDialogType = ChoiceScreenUtils.getEventDialogType();
		AbstractEvent event = AbstractDungeon.getCurrRoom().event;
		int choice_index = 0;
		if (eventDialogType == ChoiceScreenUtils.EventDialogType.IMAGE || eventDialogType == ChoiceScreenUtils.EventDialogType.ROOM) {
			for (LargeDialogOptionButton button : ChoiceScreenUtils.getEventButtons()) {
				TreeMap<String, Object> json_button = new TreeMap<>();
				json_button.put("text", removeTextFormatting(button.msg));
				json_button.put("disabled", button.isDisabled);
				json_button.put("label", ChoiceScreenUtils.getOptionName(button.msg));
				if (!button.isDisabled) {
					json_button.put("choice_index", choice_index);
					choice_index += 1;
				}
				options.add(json_button);
			}
//            state.put("body_text", removeTextFormatting(UpdateBodyTextPatch.bodyText));
		} else if (event instanceof GremlinMatchGame) {
			ArrayList<AbstractCard> orderedCards = new ArrayList<>(GremlinMatchGamePatch.cards);
			orderedCards.sort(Comparator.comparingInt(c -> GremlinMatchGamePatch.cardPositions.get(c.uuid)));
			AbstractCard chosenCard = ReflectionHacks.getPrivate(event, GremlinMatchGame.class, "chosenCard");
			for (AbstractCard card : orderedCards) {
				if (GremlinMatchGamePatch.matchedCards.contains(card.uuid)) {
					// This card was already matched. "Remove" it from the option grid.
					options.add(null);
				} else {
					TreeMap<String, Object> option = new TreeMap<>();
					if (GremlinMatchGamePatch.revealedCards.contains(card.uuid)) {
						option.put("card", getCardName(card));
					} else {
						// The user doesn't know which card this is yet.
						option.put("card", null);
					}

					if (chosenCard != null && chosenCard.uuid == card.uuid) {
						option.put("selected", true);
					}

					options.add(option);
				}
			}
		} else {
			for (String misc_option : ChoiceScreenUtils.getEventScreenChoices()) {
				TreeMap<String, Object> json_button = new TreeMap<>();
				json_button.put("text", misc_option);
				json_button.put("disabled", false);
				json_button.put("label", misc_option);
				json_button.put("choice_index", choice_index);
				choice_index += 1;
				options.add(json_button);
			}
			state.put("body_text", "");
		}
		state.put("options", options);

		state.put("event_name", ReflectionHacks.getPrivateStatic(event.getClass(), "NAME"));
		if (event instanceof NeowEvent) {
			state.put("event_id", "Neow Event");
			int bossCount = ReflectionHacks.getPrivate(event, event.getClass(), "bossCount");
			state.put("is_mini_blessing", bossCount == 0);
		} else {
			try {
				// AbstractEvent does not have a static "ID" field, but all of the events in the base game do.
				Field targetField = event.getClass().getDeclaredField("ID");
				state.put("event_id", (String) targetField.get(null));
			} catch (NoSuchFieldException | IllegalAccessException e) {
				state.put("event_id", "");
			}
			state.put("event_id", ReflectionHacks.getPrivateStatic(event.getClass(), "ID"));
		}
		return state;
	}

	private static TreeMap<String, Object> getCardRewardState() {
		TreeMap<String, Object> state = new TreeMap<>();
		state.put("bowl_available", ChoiceScreenUtils.isBowlAvailable());
		state.put("skip_available", ChoiceScreenUtils.isCardRewardSkipAvailable());
		ArrayList<Object> cardRewardJson = new ArrayList<>();
		for (AbstractCard card : AbstractDungeon.cardRewardScreen.rewardGroup) {
			cardRewardJson.add(getCardName(card));
		}
		state.put("cards", cardRewardJson);
		return state;
	}

	private static ArrayList<Object> getRewardState() {
		ArrayList<Object> rewards = new ArrayList<>();
		for (RewardItem reward : AbstractDungeon.combatRewardScreen.rewards) {
			rewards.add(GameStateConverter.convertRewardToJson(reward));
		}
		return rewards;
	}

	public static TreeMap<String, Object> convertRewardToJson(RewardItem reward) {
		TreeMap<String, Object> jsonReward = new TreeMap<>();
		jsonReward.put("reward_type", reward.type.name());
		switch (reward.type) {
			case GOLD:
			case STOLEN_GOLD:
				jsonReward.put("gold", reward.goldAmt + reward.bonusGold);
				break;
			case RELIC:
				jsonReward.put("relic", convertRelicToJson(reward.relic));
				break;
			case POTION:
				jsonReward.put("potion", convertPotionToJson(reward.potion));
				break;
			case SAPPHIRE_KEY:
				jsonReward.put("link", convertRelicToJson(reward.relicLink.relic));
				break;
			case CARD:
				ArrayList<String> cards = new ArrayList<>();
				for (AbstractCard card : reward.cards) {
					cards.add(GameStateConverter.getCardName(card));
				}
				jsonReward.put("cards", cards);
				break;
		}
		return jsonReward;
	}

	private static TreeMap<String, Object> getMapScreenState() {
		TreeMap<String, Object> state = new TreeMap<>();
		if (AbstractDungeon.getCurrMapNode() != null) {
			state.put("current_node", convertMapRoomNodeToJson(AbstractDungeon.getCurrMapNode()));
		}
		ArrayList<Object> nextNodesJson = new ArrayList<>();
		for (MapRoomNode node : ChoiceScreenUtils.getMapScreenNodeChoices()) {
			nextNodesJson.add(convertMapRoomNodeToJson(node));
		}
		if (ChoiceScreenUtils.bossNodeAvailable()) {
			TreeMap<String, Object> boss = getBossRoomCoordinates();
			boss.put("symbol", "B");
			nextNodesJson.add(boss);
		}
		state.put("next_nodes", nextNodesJson);
		return state;
	}

	private static TreeMap<String, Object> getBossRewardState() {
		TreeMap<String, Object> state = new TreeMap<>();
		ArrayList<Object> bossRelics = new ArrayList<>();
		for (AbstractRelic relic : AbstractDungeon.bossRelicScreen.relics) {
			bossRelics.add(convertRelicToJson(relic));
		}
		state.put("relics", bossRelics);
		return state;
	}

	private static TreeMap<String, Object> getShopState() {
		TreeMap<String, Object> state = new TreeMap<>();
		ArrayList<Object> shopCards = new ArrayList<>();
		ArrayList<Object> shopRelics = new ArrayList<>();
		ArrayList<Object> shopPotions = new ArrayList<>();
		for (AbstractCard card : ChoiceScreenUtils.getShopScreenCards()) {
			TreeMap<String, Object> jsonCard = new TreeMap<>();
			jsonCard.put("name", GameStateConverter.getCardName(card));
			jsonCard.put("price", card.price);
			shopCards.add(jsonCard);
		}
		for (StoreRelic relic : ChoiceScreenUtils.getShopScreenRelics()) {
			TreeMap<String, Object> jsonRelic = convertRelicToJson(relic.relic);
			jsonRelic.put("price", relic.price);
			shopRelics.add(jsonRelic);
		}
		for (StorePotion potion : ChoiceScreenUtils.getShopScreenPotions()) {
			TreeMap<String, Object> jsonPotion = new TreeMap<>();
			jsonPotion.put("id", potion.potion.ID);
			jsonPotion.put("price", potion.price);
			shopPotions.add(jsonPotion);
		}
		state.put("cards", shopCards);
		state.put("relics", shopRelics);
		state.put("potions", shopPotions);
		int purgeCost = AbstractDungeon.shopScreen.purgeAvailable ? ShopScreen.actualPurgeCost : -1;
		state.put("purge_cost", purgeCost);
		return state;
	}

	private static TreeMap<String, Object> getGridState() {
		TreeMap<String, Object> state = new TreeMap<>();
		ArrayList<Object> gridJson = new ArrayList<>();
		ArrayList<Object> gridSelectedJson = new ArrayList<>();
		ArrayList<AbstractCard> gridCards = ChoiceScreenUtils.getGridScreenCards();
		GridCardSelectScreen screen = AbstractDungeon.gridSelectScreen;
		for (AbstractCard card : gridCards) {
			gridJson.add(getCardName(card));
		}
		for (AbstractCard card : screen.selectedCards) {
			gridSelectedJson.add(getCardName(card));
		}
		int numCards = (int) ReflectionHacks.getPrivate(screen, GridCardSelectScreen.class, "numCards");
		boolean forUpgrade = (boolean) ReflectionHacks.getPrivate(screen, GridCardSelectScreen.class, "forUpgrade");
		boolean forTransform = (boolean) ReflectionHacks.getPrivate(screen, GridCardSelectScreen.class, "forTransform");
		boolean forPurge = (boolean) ReflectionHacks.getPrivate(screen, GridCardSelectScreen.class, "forPurge");
		state.put("cards", gridJson);
		state.put("selected_cards", gridSelectedJson);
		state.put("num_cards", numCards);
		state.put("any_number", screen.anyNumber);
		state.put("for_upgrade", forUpgrade);
		state.put("for_transform", forTransform);
		state.put("for_purge", forPurge);
		state.put("confirm_up", screen.confirmScreenUp || screen.isJustForConfirming);
		return state;
	}

	private static TreeMap<String, Object> getHandSelectState() {
		TreeMap<String, Object> state = new TreeMap<>();
		ArrayList<Object> handJson = new ArrayList<>();
		ArrayList<Object> selectedJson = new ArrayList<>();
		ArrayList<AbstractCard> handCards = AbstractDungeon.player.hand.group;
		// As far as I can tell, this comment is a Java 8 analogue of a Python list comprehension? I think just looping is more readable.
		// handJson = handCards.stream().map(GameStateConverter::getCardName).collect(Collectors.toCollection(ArrayList::new));
		for (AbstractCard card : handCards) {
			handJson.add(getCardName(card));
		}
		state.put("hand", handJson);
		ArrayList<AbstractCard> selectedCards = AbstractDungeon.handCardSelectScreen.selectedCards.group;
		for (AbstractCard card : selectedCards) {
			selectedJson.add(getCardName(card));
		}
		state.put("selected", selectedJson);
		state.put("max_cards", AbstractDungeon.handCardSelectScreen.numCardsToSelect);
		state.put("can_pick_zero", AbstractDungeon.handCardSelectScreen.canPickZero);
		return state;
	}

	private static TreeMap<String, Object> getScreenState() {
		ChoiceScreenUtils.ChoiceType screenType = ChoiceScreenUtils.getCurrentChoiceType();
		switch (screenType) {
			case EVENT:
				return getEventState();
			case CHEST:
			case REST:
				return getRoomState();
			case CARD_REWARD:
				return getCardRewardState();
			case MAP:
				return getMapScreenState();
			case BOSS_REWARD:
				return getBossRewardState();
			case GRID:
				return getGridState();
			case HAND_SELECT:
				return getHandSelectState();
		}
		return new TreeMap<>();
	}

	private static TreeMap<String, Object> getCombatState() {
		TreeMap<String, Object> state = new TreeMap<>();

		ArrayList<Object> monsters = new ArrayList<>();
		for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
			monsters.add(convertMonsterToJson(monster));
		}
		state.put("monsters", monsters);

		ArrayList<Object> draw_pile = new ArrayList<>();
		for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
			draw_pile.add(getCardName(card));
		}
		if (draw_pile.size() > 0) {
			state.put("draw_pile", draw_pile);
		}

		ArrayList<Object> discard_pile = new ArrayList<>();
		for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
			discard_pile.add(getCardName(card));
		}
		if (discard_pile.size() > 0) {
			state.put("discard_pile", discard_pile);
		}

		ArrayList<Object> exhaust_pile = new ArrayList<>();
		for (AbstractCard card : AbstractDungeon.player.exhaustPile.group) {
			exhaust_pile.add(getCardName(card));
		}
		if (exhaust_pile.size() > 0) {
			state.put("exhaust_pile", exhaust_pile);
		}

		ArrayList<Object> hand = new ArrayList<>();
		for (AbstractCard card : AbstractDungeon.player.hand.group) {
			hand.add(getCardName(card));
		}
		if (hand.size() > 0) {
			state.put("hand", hand);
		}

		ArrayList<Object> limbo = new ArrayList<>();
		for (AbstractCard card : AbstractDungeon.player.limbo.group) {
			limbo.add(getCardName(card));
		}
		if (limbo.size() > 0) {
			state.put("limbo", limbo);
		}

		state.put("player", convertPlayerToJson(AbstractDungeon.player));
		return state;
	}

	private static ArrayList<Object> convertMapToJson() {
		ArrayList<ArrayList<MapRoomNode>> map = AbstractDungeon.map;
		ArrayList<Object> jsonMap = new ArrayList<>();
		for (ArrayList<MapRoomNode> layer : map) {
			for (MapRoomNode node : layer) {
				if (node.hasEdges()) {
					TreeMap<String, Object> json_node = convertMapRoomNodeToJson(node);
					ArrayList<Object> json_children = new ArrayList<>();
					ArrayList<Object> json_parents = new ArrayList<>();
					for (MapEdge edge : node.getEdges()) {
						if (edge.srcX == node.x && edge.srcY == node.y) {
							json_children.add(convertCoordinatesToJson(edge.dstX, edge.dstY));
						} else {
							json_parents.add(convertCoordinatesToJson(edge.srcX, edge.srcY));
						}
					}

					json_node.put("parents", json_parents);
					json_node.put("children", json_children);
					jsonMap.add(json_node);
				}
			}
		}

		return jsonMap;
	}

	private static TreeMap<String, Object> convertCoordinatesToJson(int x, int y) {
		TreeMap<String, Object> jsonNode = new TreeMap<>();
		jsonNode.put("x", x);
		jsonNode.put("y", y);
		return jsonNode;
	}

	public static TreeMap<String, Object> convertMapRoomNodeToJson(MapRoomNode node) {
		TreeMap<String, Object> jsonNode = convertCoordinatesToJson(node.x, node.y);
		jsonNode.put("symbol", node.getRoomSymbol(true));
		return jsonNode;
	}

	public static TreeMap<String, Object> getBossRoomCoordinates() {
		// The y-coordinate matches the edge that STS creates when generating the dungeon map.
		int y = 16;
		if (AbstractDungeon.id.equals("TheEnding")) {
			y = 3;
		}
		return convertCoordinatesToJson(3, y);
	}

	public static String getCardName(AbstractCard card) {
		String name = card.name;
		// if (card.upgraded) {
		// 	name += "+";
		// }
		// if (card.timesUpgraded > 1) {
		// 	name += Integer.toString(card.timesUpgraded);
		// }
		return name;
	}

	public static TreeMap<String, Object> convertMonsterToJson(AbstractMonster monster) {
		TreeMap<String, Object> jsonMonster = new TreeMap<>();
		jsonMonster.put("id", monster.id);
		jsonMonster.put("hp_current", monster.currentHealth);
		jsonMonster.put("hp_max", monster.maxHealth);
		if (monster.isDeadOrEscaped()) {
			jsonMonster.put("is_gone", true);
		} else {
			if (AbstractDungeon.player.hasRelic(RunicDome.ID)) {
				jsonMonster.put("intent", AbstractMonster.Intent.NONE);
			} else {
				jsonMonster.put("intent", monster.intent.name());
				EnemyMoveInfo moveInfo = (EnemyMoveInfo) ReflectionHacks.getPrivate(monster, AbstractMonster.class, "move");
				if (moveInfo != null) {
					int damage = (int) ReflectionHacks.getPrivate(monster, AbstractMonster.class, "intentDmg");
					if (damage > 0) {
						jsonMonster.put("damage", damage);
					}
					if (moveInfo.multiplier > 1) {
						jsonMonster.put("hits", moveInfo.multiplier);
					}
				}
			}
			if (monster.halfDead) {
				// Applies to monsters that can be "reborn", e.g. Darklings or Awakened One. Indicates they
				// currently can't be attacked / won't attack.
				jsonMonster.put("half_dead", true);
			}
			if (monster.currentBlock > 0) {
				jsonMonster.put("block", monster.currentBlock);
			}

			ArrayList<Object> powers = convertCreaturePowersToJson(monster);
			if (!powers.isEmpty()) {
				jsonMonster.put("powers", powers);
			}
		}

		return jsonMonster;
	}

	private static TreeMap<String, Object> convertPlayerToJson(AbstractPlayer player) {
		TreeMap<String, Object> jsonPlayer = new TreeMap<>();

		ArrayList<Object> powers = convertCreaturePowersToJson(player);
		if (powers.size() > 0) {
			jsonPlayer.put("powers", powers);
		}
		jsonPlayer.put("energy", EnergyPanel.totalCount);
		if (player.currentBlock > 0) {
			jsonPlayer.put("block", player.currentBlock);
		}
		ArrayList<Object> orbs = new ArrayList<>();
		for (AbstractOrb orb : player.orbs) {
			orbs.add(convertOrbToJson(orb));
		}
		if (orbs.size() > 0) {
			jsonPlayer.put("orbs", orbs);
		}
		// Whether the player is facing backwards (only relevant in shield + spear).
		if (player.flipHorizontal) {
			jsonPlayer.put("flipped", true);
		}

		return jsonPlayer;
	}

	/**
	 * Checks whether the given object has the specified field. If so, returns the field's value. Else returns null.
	 *
	 * @param object    The object used to look for the specified field
	 * @param fieldName The field that we want to access
	 * @return The value of the field, if present, or else null.
	 */
	private static Object getFieldIfExists(Object object, String fieldName) {
		Class objectClass = object.getClass();
		for (Field field : objectClass.getDeclaredFields()) {
			if (field.getName().equals(fieldName)) {
				try {
					field.setAccessible(true);
					return field.get(object);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					return null;
				}
			}
		}
		return null;
	}

	private static ArrayList<Object> convertCreaturePowersToJson(AbstractCreature creature) {
		ArrayList<Object> powers = new ArrayList<>();
		for (AbstractPower power : creature.powers) {
			TreeMap<String, Object> json_power = new TreeMap<>();
			json_power.put("id", power.ID);
			json_power.put("amount", power.amount);

			powers.add(json_power);
		}
		return powers;
	}

	private static TreeMap<String, Object> convertRelicToJson(AbstractRelic relic) {
		TreeMap<String, Object> jsonRelic = new TreeMap<>();
		jsonRelic.put("id", relic.relicId);
		if (relic.counter > -1) {
			jsonRelic.put("counter", relic.counter);
		}
		return jsonRelic;
	}

	public static String convertPotionToJson(AbstractPotion potion) {
		return potion.ID;
	}

	private static TreeMap<String, Object> convertOrbToJson(AbstractOrb orb) {
		TreeMap<String, Object> jsonOrb = new TreeMap<>();
		jsonOrb.put("id", orb.ID);
		jsonOrb.put("name", orb.name);
		jsonOrb.put("evoke_amount", orb.evokeAmount);
		jsonOrb.put("passive_amount", orb.passiveAmount);
		return jsonOrb;
	}

	private static String getNoteForYourselfCard() {
		AbstractCard card = CardLibrary.getCard(CardCrawlGame.playerPref.getString("NOTE_CARD", "Iron Wave"));
		if (card == null) {
			card = new IronWave();
		} else {
			card = card.makeCopy();
		}
		for(int i = 0; i < CardCrawlGame.playerPref.getInteger("NOTE_UPGRADE", 0); ++i) {
			card.upgrade();
		}
		return getCardName(card);
	}
}
