package serializationmod.patches.perf;

public class SuperFastModePatch {
	// @SpirePatch(clz = com.esotericsoftware.spine.AnimationState.class, method = "update", requiredModId = "superfastmode", optional = true)
	// public static class Update {
	// 	@SpirePrefixPatch
	// 	public static void Prefix(Object o, @ByRef(type = "float") float[] delta) {
	// 		if (delta[0] == SuperFastMode.getDelta()) {
	// 			delta[0] = SuperFastMode.getMultDelta();
	// 		}
	// 	}
	// }
	//
	// // The following patches speeds up a few animations that are otherwise set to normal speed by superfastmode:
	// // https://github.com/Skrelpoid/SuperFastMode/blob/master/src/main/java/skrelpoid/superfastmode/patches/DefaultDeltaPatches.java
	// //
	// @SpirePatch(clz = CardCrawlGame.class, method = "updateFade", requiredModId = "superfastmode", optional = true)
	// // Makes time not increase by multiplier.
	// // @SpirePatch(clz = com.megacrit.cardcrawl.dungeons.AbstractDungeon.class, method = "update", requiredModId = "superfastmode", optional = true)
	// // Fixes mouse events not registering on map and flickering "Select a Starting Room"
	// // @SpirePatch(clz = com.megacrit.cardcrawl.screens.DungeonMapScreen.class, method = "update", requiredModId = "superfastmode", optional = true)
	// // @SpirePatch(clz = com.megacrit.cardcrawl.screens.DungeonMapScreen.class, method = "updateAnimation", requiredModId = "superfastmode", optional = true)
	// // @SpirePatch(clz = com.megacrit.cardcrawl.screens.DungeonMapScreen.class, method = "updateMouse", requiredModId = "superfastmode", optional = true)
	// // @SpirePatch(clz = com.megacrit.cardcrawl.screens.DungeonMapScreen.class, method = "oscillateColor", requiredModId = "superfastmode", optional = true)
	// // Display SpeechBubbles long enough to read
	// @SpirePatch(clz = DialogWord.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = DialogWord.class, method = "applyEffects", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.ui.SpeechWord.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.ui.SpeechWord.class, method = "applyEffects", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.SpeechBubble.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.SpeechBubble.class, method = "updateScale", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.CloudBubble.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.ThoughtBubble.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.InfiniteSpeechBubble.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.InfiniteSpeechBubble.class, method = "updateScale", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.ShopSpeechBubble.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.ShopSpeechBubble.class, method = "updateScale", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.SpeechTextEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.MegaSpeechBubble.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.MegaSpeechBubble.class, method = "updateScale", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.MegaDialogTextEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// // Next prevent some flickering and make UI independent from multiplied delta
	// @SpirePatch(clz = CardSelectConfirmButton.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = SingingBowlButton.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = SkipCardButton.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.ui.buttons.DynamicBanner.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.TintEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.ui.buttons.CancelButton.class, method = "update", requiredModId = "superfastmode", optional = true)
	// // @SpirePatch(clz = com.megacrit.cardcrawl.ui.buttons.CancelButton.class, method = "updateGlow", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.screens.mainMenu.MenuCancelButton.class, method = "update", requiredModId = "superfastmode", optional = true)
	// // @SpirePatch(clz = com.megacrit.cardcrawl.screens.mainMenu.MenuCancelButton.class, method = "updateGlow", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.ui.buttons.ConfirmButton.class, method = "update", requiredModId = "superfastmode", optional = true)
	// // @SpirePatch(clz = com.megacrit.cardcrawl.ui.buttons.ConfirmButton.class, method = "updateGlow", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.ui.buttons.EndTurnButton.class, method = "update", requiredModId = "superfastmode", optional = true)
	// // @SpirePatch(clz = com.megacrit.cardcrawl.ui.buttons.EndTurnButton.class, method = "glow", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.ui.buttons.GridSelectConfirmButton.class, method = "update", requiredModId = "superfastmode", optional = true)
	// // @SpirePatch(clz = com.megacrit.cardcrawl.ui.buttons.GridSelectConfirmButton.class, method = "updateGlow", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.ui.buttons.ProceedButton.class, method = "update", requiredModId = "superfastmode", optional = true)
	// // @SpirePatch(clz = com.megacrit.cardcrawl.map.MapRoomNode.class, method = "oscillateColor", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.ui.panels.TopPanel.class, method = "updateSettingsButtonLogic", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.ui.panels.TopPanel.class, method = "updateDeckViewButtonLogic", requiredModId = "superfastmode", optional = true)
	// // @SpirePatch(clz = com.megacrit.cardcrawl.ui.panels.TopPanel.class, method = "updateMapButtonLogic", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.ui.panels.TopPanel.class, method = "updatePotions", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.DiscardGlowEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.ui.panels.DiscardPilePanel.class, method = "updatePositions", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.ExhaustPileParticle.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.ui.panels.ExhaustPanel.class, method = "updatePositions", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.GameDeckGlowEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.ui.panels.DrawPilePanel.class, method = "updatePositions", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.cards.AbstractCard.class, method = "updateGlow", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.cards.AbstractCard.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.cards.AbstractCard.class, method = "updateHoverLogic", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.cards.AbstractCard.class, method = "updateColor", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.cards.AbstractCard.class, method = "updateTransparency", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.cardManip.CardGlowBorder.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.rewards.RewardItem.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.RewardGlowEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.orbs.EmptyOrbSlot.class, method = "updateAnimation", requiredModId = "superfastmode", optional = true)
	// //Energy
	// @SpirePatch(clz = com.megacrit.cardcrawl.ui.panels.EnergyPanel.class, method = "updateVfx", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.ui.panels.energyorb.EnergyOrbBlue.class, method = "updateOrb", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.ui.panels.energyorb.EnergyOrbRed.class, method = "updateOrb", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.ui.panels.energyorb.EnergyOrbGreen.class, method = "updateOrb", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = basemod.abstracts.CustomEnergyOrb.class, method = "updateOrb", requiredModId = "superfastmode", optional = true)
	// // Intents
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.BobEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.monsters.AbstractMonster.class, method = "updateIntentVFX", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.monsters.AbstractMonster.class, method = "updateIntent", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.monsters.AbstractMonster.class, method = "render", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.monsters.AbstractMonster.class, method = "renderName", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.monsters.AbstractMonster.class, method = "renderIntent", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.monsters.AbstractMonster.class, method = "updateDeathAnimation", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.monsters.AbstractMonster.class, method = "updateEscapeAnimation", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.combat.StunStarEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.combat.UnknownParticleEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.ShieldParticleEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.combat.BuffParticleEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.DebuffParticleEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// // Orbs
	// @SpirePatch(clz = com.megacrit.cardcrawl.orbs.Dark.class, method = "updateAnimation", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.orbs.Frost.class, method = "updateAnimation", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = FrostOrbPassiveEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.orbs.Lightning.class, method = "updateAnimation", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.orbs.Plasma.class, method = "updateAnimation", requiredModId = "superfastmode", optional = true)
	// // Relic
	// @SpirePatch(clz = com.megacrit.cardcrawl.relics.AbstractRelic.class, method = "updateFlash", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.relics.AbstractRelic.class, method = "updateAnimation", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.relics.AbstractRelic.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.FloatyEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// // MainMenuScreen
	// @SpirePatch(clz = com.megacrit.cardcrawl.scenes.TitleBackground.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.scenes.TitleBackground.class, method = "updateFlame", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.scenes.TitleBackground.class, method = "updateDust", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = TitleCloud.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.scene.LogoFlameEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.scene.TitleDustEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.ui.panels.RenamePopup.class, method = "update", requiredModId = "superfastmode", optional = true)
	// // Credits
	// @SpirePatch(clz = com.megacrit.cardcrawl.credits.CreditsScreen.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.credits.CreditsScreen.class, method = "updateFade", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.credits.CreditsScreen.class, method = "skipLogic", requiredModId = "superfastmode", optional = true)
	// // Splash
	// @SpirePatch(clz = com.megacrit.cardcrawl.screens.splash.SplashScreen.class, method = "update", requiredModId = "superfastmode", optional = true)
	// // Text effects
	// @SpirePatch(clz = com.megacrit.cardcrawl.events.GenericEventDialog.class, method = "bodyTextEffectCN", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.events.GenericEventDialog.class, method = "bodyTextEffect", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.events.RoomEventDialog.class, method = "bodyTextEffectCN", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.events.RoomEventDialog.class, method = "bodyTextEffect", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.ui.DialogWord.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.ui.DialogWord.class, method = "applyEffects", requiredModId = "superfastmode", optional = true)
	// // ShopScreen
	// @SpirePatch(clz = com.megacrit.cardcrawl.shop.ShopScreen.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.shop.ShopScreen.class, method = "updateSpeech", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.shop.ShopScreen.class, method = "updateHand", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.shop.ShopScreen.class, method = "updateRug", requiredModId = "superfastmode", optional = true)
	// // RestRoom
	// @SpirePatch(clz = com.megacrit.cardcrawl.rooms.CampfireUI.class, method = "updateFire", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.campfire.CampfireBurningEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.campfire.CampfireBubbleEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// // Particles Exordium
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.scene.DustEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.scene.BottomFogEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.scene.InteractableTorchEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.scene.TorchParticleSEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.scene.LightFlareSEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.scene.TorchParticleMEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.scene.LightFlareMEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.scene.TorchParticleLEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.scene.LightFlareLEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// // Particles The City
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.scene.CeilingDustCloudEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.FallingDustEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.scenes.TheCityScene.class, method = "updateParticles", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.scene.FireFlyEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// // Chests
	// @SpirePatch(clz = com.megacrit.cardcrawl.rooms.TreasureRoom.class, method = "updateShiny", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.ChestShineEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.scene.SpookyChestEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.scene.SpookierChestEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.BossChestShineEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.screens.select.BossRelicSelectScreen.class, method = "update", requiredModId = "superfastmode", optional = true)
	// // DeathScreen
	// @SpirePatch(clz = com.megacrit.cardcrawl.vfx.DeathScreenFloatyEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// // GridSelectScreen (Tramsform and Upgrade)
	// @SpirePatch(clz = com.megacrit.cardcrawl.screens.select.GridCardSelectScreen.class, method = "update", requiredModId = "superfastmode", optional = true)
	// // Math
	// @SpirePatch(clz = com.megacrit.cardcrawl.helpers.MathHelper.class, method = "mouseLerpSnap", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.helpers.MathHelper.class, method = "cardLerpSnap", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.helpers.MathHelper.class, method = "cardScaleLerpSnap", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.helpers.MathHelper.class, method = "uiLerpSnap", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.helpers.MathHelper.class, method = "orbLerpSnap", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.helpers.MathHelper.class, method = "scaleLerpSnap", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.helpers.MathHelper.class, method = "fadeLerpSnap", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.helpers.MathHelper.class, method = "popLerpSnap", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.helpers.MathHelper.class, method = "angleLerpSnap", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.helpers.MathHelper.class, method = "slowColorLerpSnap", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = com.megacrit.cardcrawl.helpers.MathHelper.class, method = "scrollSnapLerpSpeed", requiredModId = "superfastmode", optional = true)
	// // Blights
	// @SpirePatch(clz = AbstractBlight.class, method = "updateAnimation", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = AbstractBlight.class, method = "updateFlash", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = AbstractBlight.class, method = "update", requiredModId = "superfastmode", optional = true)
	// //BattleStart
	// @SpirePatch(clz = BattleStartEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = BattleStartEffect.class, method = "updateSwords", requiredModId = "superfastmode", optional = true)
	// // AbstractCreature
	// @SpirePatch(clz = AbstractCreature.class, method = "updateFastAttackAnimation", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = AbstractCreature.class, method = "updateSlowAttackAnimation", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = AbstractCreature.class, method = "updateFastShakeAnimation", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = AbstractCreature.class, method = "updateShakeAnimation", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = AbstractCreature.class, method = "updateHopAnimation", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = AbstractCreature.class, method = "updateJumpAnimation", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = AbstractCreature.class, method = "updateStaggerAnimation", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = AbstractCreature.class, method = "updateHbHoverFade", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = AbstractCreature.class, method = "updateBlockAnimations", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = AbstractCreature.class, method = "updateHbPopInAnimation", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = AbstractCreature.class, method = "updateHbDamageAnimation", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = AbstractCreature.class, method = "updateReticle", requiredModId = "superfastmode", optional = true)
	// // Misc
	// @SpirePatch(clz = PowerIconShowEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = EnemyTurnEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = GainPennyEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = GameDeckGlowEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = PlayerTurnEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = RoomShineEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = RoomShineEffect2.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = AbstractScene.class, method = "updateBgOverlay", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = AbstractPower.class, method = "updateFontScale", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = DiscardPilePanel.class, method = "updatePositions", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = DiscardPilePanel.class, method = "updatePop", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = DrawPilePanel.class, method = "updatePositions", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = DrawPilePanel.class, method = "updatePop", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = PowerBuffEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = PowerDebuffEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = TextAboveCreatureEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = FlashAtkImgEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = FlashIntentEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = FlashPotionEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = BlockedNumberEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = BottomFogEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = BottomBgPanel.class, method = "updatePositions", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = CardGlowBorder.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = HealNumberEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = FlameAnimationEffect.class, method = "update", requiredModId = "superfastmode", optional = true)
	// // @SpirePatch(clz = MapRoomNode.class, method = "update", requiredModId = "superfastmode", optional = true)
	// // @SpirePatch(clz = MapRoomNode.class, method = "updateEmerald", requiredModId = "superfastmode", optional = true)
	// // @SpirePatch(clz = MapRoomNode.class, method = "oscillateColor", requiredModId = "superfastmode", optional = true)
	// @SpirePatch(clz = SpriterAnimation.class, method = "renderSprite", requiredModId = "superfastmode", optional = true)
	// public static class DeltaPatch {
	// 	public static ExprEditor Instrument() {
	// 		return new ExprEditor() {
	// 			@Override
	// 			public void edit(MethodCall m) throws CannotCompileException {
	// 				try {
	// 					SerializationMod.logger.info("maybe editing {} ; {} ; {} ; {}", m.toString(), m.getMethod().getLongName(), m.getClassName(), m.getMethodName());
	// 				} catch (Exception e) {
	// 					SerializationMod.logger.info("exception {} while editing {}", e.toString(), m.toString());
	// 				}
	// 				if (m.getClassName().equals("skrelpoid.superfastmode.SuperFastMode") && m.getMethodName().equals("getDelta")) {
	// 					m.replace("{ $_ = skrelpoid.superfastmode.SuperFastMode.getMultDelta(); }");
	// 				}
	// 			}
	// 		};
	// 	}
	// }
}
