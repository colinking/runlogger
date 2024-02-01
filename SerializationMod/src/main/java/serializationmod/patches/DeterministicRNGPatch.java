package serializationmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.unique.DiscoveryAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import serializationmod.SerializationMod;

import java.util.ArrayList;

public class DeterministicRNGPatch {
	// All randomness in a Slay the Spire run is generated from the seed EXCEPT
	// for three specific kinds of card transforms...
	//
	//   1. Living Wall event
	//   2. Transmogrifier event
	//   3. Neow's "Transform a card" bonus
	//
	// ...and DiscoveryAction's card generation (which triggers an RNG call on every update() call).
	//
	// (note: all other transforms (e.g. Neow's "Transform 2 cards" bonus) are determined
	// by the seed.)
	//
	// In other words, you can reproduce an arbitrary Slay the Spire run by "replaying" the
	// actions someone took in a run, UNLESS one of the events above is encountered. These
	// events use an unseeded RNG and will produce different results every time. This could
	// be abused e.g. to guarantee a specific transform by saving+continuing to reroll a
	// transform until the desired card is produced.
	//
	// This seems like an oversight, as all other card transforms (including Neow's
	// "Transform 2 cards" bonus) all used seeded RNGs.
	//
	// This patch replaces the unseeded RNG used by these three transform events with a
	// seeded, uncorrelated RNG. It also implements caching for DiscoveryAction s.t. the
	// number of RNG calls is deterministic.
	@SpirePatch(clz= AbstractDungeon.class, method="transformCard", paramtypez = {AbstractCard.class, boolean.class})
	public static class TransformPatch {
		public static SpireReturn<Void> Prefix(AbstractCard c, boolean autoUpgrade) {
			// Vanilla STS does this:
			// transformCard(c, autoUpgrade, new Random());
			// We use a seeded RNG instead:
			AbstractDungeon.transformCard(c, autoUpgrade, SerializationMod.run.deterministicTransformRNG);

			return SpireReturn.Return();
		}
	}

	@SpirePatch(clz = DiscoveryAction.class, method="generateColorlessCardChoices")
	public static class DiscoveryColorlessPatch {
		public static SpireReturn<ArrayList<AbstractCard>> Prefix(DiscoveryAction instance) {
			ArrayList<AbstractCard> cards = SerializationMod.run.discoveryCache.get(instance, null);
			if (cards != null) {
				// Return the cached cards.
				return SpireReturn.Return(cards);
			}
			// Generate new cards. These will be cached via Postfix.
			return SpireReturn.Continue();
		}

		public static ArrayList<AbstractCard> Postfix(ArrayList<AbstractCard> returnValue, DiscoveryAction instance) {
			ArrayList<AbstractCard> cards = SerializationMod.run.discoveryCache.get(instance, null);
			if (cards == null) {
				// Cache these results.
				SerializationMod.run.discoveryCache.set(instance, null, returnValue);
			}
			return returnValue;
		}
	}

	@SpirePatch(clz = DiscoveryAction.class, method="generateCardChoices", paramtypez = {AbstractCard.CardType.class})
	public static class DiscoveryCardTypePatch {
		public static SpireReturn<ArrayList<AbstractCard>> Prefix(DiscoveryAction instance, AbstractCard.CardType type) {
			ArrayList<AbstractCard> cards = SerializationMod.run.discoveryCache.get(instance, type);
			if (cards != null) {
				// Return the cached cards.
				return SpireReturn.Return(cards);
			}
			// Generate new cards. These will be cached via Postfix.
			return SpireReturn.Continue();
		}

		public static ArrayList<AbstractCard> Postfix(ArrayList<AbstractCard> returnValue, DiscoveryAction instance, AbstractCard.CardType type) {
			ArrayList<AbstractCard> cards = SerializationMod.run.discoveryCache.get(instance, type);
			if (cards == null) {
				// Cache these results.
				SerializationMod.run.discoveryCache.set(instance, null, returnValue);
			}
			return returnValue;
		}
	}

	public static class DiscoveryCache {
		private final ArrayList<CacheResult> elements;

		private static class CacheResult {
			public DiscoveryAction action;
			public AbstractCard.CardType type;
			public ArrayList<AbstractCard> cards;

			public CacheResult(DiscoveryAction action, AbstractCard.CardType type, ArrayList<AbstractCard> cards) {
				this.action = action;
				this.type = type;
				this.cards = cards;
			}
		}

		public DiscoveryCache() {
			this.elements = new ArrayList<>();
		}

		public ArrayList<AbstractCard> get(DiscoveryAction action, AbstractCard.CardType type) {
			for (CacheResult element : this.elements) {
				if (element.action == action && element.type == type) {
					return element.cards;
				}
			}
			return null;
		}

		public void set(DiscoveryAction action, AbstractCard.CardType type, ArrayList<AbstractCard> cards) {
			for (CacheResult element : this.elements) {
				if (element.action == action && element.type == type) {
					element.cards = cards;
					return;
				}
			}
			this.elements.add(new CacheResult(action, type, cards));
		}
	}
}
