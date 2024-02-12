package serializationmod;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.interfaces.PostInitializeSubscriber;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.Patcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.scannotation.AnnotationDB;

import java.io.IOException;
import java.util.*;

@SpireInitializer
public class SerializationMod implements PostInitializeSubscriber {
	public static ModInfo info;
	public static String modID;
	static { loadModInfo(); }

	public static final Logger logger = LogManager.getLogger(modID);

	// Config options
	private static SpireConfig config;
	private static final String VERBOSE_OPTION = "verbose";
	private static final boolean DEFAULT_VERBOSITY = false;

	public static RunFile run;

	public SerializationMod() {
		BaseMod.subscribe(this);
		try {
			Properties defaults = new Properties();
			defaults.put(VERBOSE_OPTION, Boolean.toString(DEFAULT_VERBOSITY));
			config = new SpireConfig(modID, "config", defaults);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// https://github.com/kiooeht/ModTheSpire/wiki/SpireInitializer
	public static void initialize() {
		new SerializationMod();
	}

	@Override
	public void receivePostInitialize() {
		setUpOptionsMenu();
	}

	private void setUpOptionsMenu() {
		ModPanel settingsPanel = new ModPanel();

		ModLabeledToggleButton verbosityOption = new ModLabeledToggleButton(
			"Show verbose log output",
			390, 720, Settings.CREAM_COLOR, FontHelper.charDescFont,
			getVerbosityOption(), settingsPanel, modLabel -> {
		},
			modToggleButton -> {
				if (config != null) {
					config.setBool(VERBOSE_OPTION, modToggleButton.enabled);
					try {
						config.save();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		settingsPanel.addUIElement(verbosityOption);

		// Set up the mod information displayed in the in-game mods menu.
		BaseMod.registerModBadge(ImageMaster.loadImage(modID+".png"), info.Name, info.Authors[0], info.Description, settingsPanel);
	}

	private static boolean getVerbosityOption() {
		if (config == null) {
			return DEFAULT_VERBOSITY;
		}
		return config.getBool(VERBOSE_OPTION);
	}

	// This determines the mod's ID based on information stored by ModTheSpire.
	private static void loadModInfo() {
		Optional<ModInfo> infos = Arrays.stream(Loader.MODINFOS).filter((modInfo)->{
			AnnotationDB annotationDB = Patcher.annotationDBMap.get(modInfo.jarURL);
			if (annotationDB == null)
				return false;
			Set<String> initializers = annotationDB.getAnnotationIndex().getOrDefault(SpireInitializer.class.getName(), Collections.emptySet());
			return initializers.contains(SerializationMod.class.getName());
		}).findFirst();
		if (infos.isPresent()) {
			info = infos.get();
			modID = info.ID;
		}
		else {
			throw new RuntimeException("Failed to determine mod info/ID based on initializer.");
		}
	}
}
