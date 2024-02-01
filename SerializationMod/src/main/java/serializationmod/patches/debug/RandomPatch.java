package serializationmod.patches.debug;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.random.Random;
import serializationmod.SerializationMod;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class RandomPatch {
	@SpirePatch(clz= Random.class, method="random", paramtypez = {int.class})
	@SpirePatch(clz= Random.class, method="random", paramtypez = {int.class, int.class})
	@SpirePatch(clz= Random.class, method="random", paramtypez = {long.class})
	@SpirePatch(clz= Random.class, method="random", paramtypez = {long.class, long.class})
	@SpirePatch(clz= Random.class, method="randomLong", paramtypez = {})
	@SpirePatch(clz= Random.class, method="randomBoolean", paramtypez = {})
	@SpirePatch(clz= Random.class, method="randomBoolean", paramtypez = {float.class})
	@SpirePatch(clz= Random.class, method="random", paramtypez = {})
	@SpirePatch(clz= Random.class, method="random", paramtypez = {float.class})
	@SpirePatch(clz= Random.class, method="random", paramtypez = {float.class, float.class})
	public static class Patch {
		public static void Postfix(Random instance) {
			if (SerializationMod.run == null) {
				// A run has not started yet. Ignore RNG calls outside of runs.
				return;
			}

			String msg = "";
			msg += ZonedDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'h:m:ss.SZ"));
			msg += " log=" + SerializationMod.run.lines;
			msg += " " + RandomPatch.getRNGName(instance) + "=" + instance.counter;

			StackTraceElement[] trace = Thread.currentThread().getStackTrace();
			for (StackTraceElement e : trace) {
				// Ignore a few irrelevant trace elements
				if (e.getClassName().startsWith("com.badlogic.gdx.backends.lwjgl.LwjglApplication") || e.getClassName().startsWith("serializationmod.patches.debug.RandomPatch") || e.getClassName().startsWith("java.lang.Thread")) {
					continue;
				}

				msg += "\n" + e.toString();
			}

			SerializationMod.run.debugLog(msg);
		}
	}

	private static String getRNGName(Random instance) {
		if (instance == AbstractDungeon.monsterRng) {
			return "monsterRng";
		}
		if (instance == AbstractDungeon.mapRng) {
			return "mapRng";
		}
		if (instance == AbstractDungeon.eventRng) {
			return "eventRng";
		}
		if (instance == AbstractDungeon.merchantRng) {
			return "merchantRng";
		}
		if (instance == AbstractDungeon.cardRng) {
			return "cardRng";
		}
		if (instance == AbstractDungeon.treasureRng) {
			return "treasureRng";
		}
		if (instance == AbstractDungeon.relicRng) {
			return "relicRng";
		}
		if (instance == AbstractDungeon.potionRng) {
			return "potionRng";
		}
		if (instance == AbstractDungeon.monsterHpRng) {
			return "monsterHpRng";
		}
		if (instance == AbstractDungeon.aiRng) {
			return "aiRng";
		}
		if (instance == AbstractDungeon.shuffleRng) {
			return "shuffleRng";
		}
		if (instance == AbstractDungeon.cardRandomRng) {
			return "cardRandomRng";
		}
		if (instance == AbstractDungeon.miscRng) {
			return "miscRng";
		}
		if (instance == NeowEvent.rng) {
			return "neowRng";
		}
		if (instance == SerializationMod.run.deterministicTransformRNG) {
			return "trulyRandomRNG";
		}

		// usually occurs when an RNG is temporarily duplicated (e.g. to check for combat for juzu bracelet)
		return "<unknown rng>";
	}
}
