package serializationmod;

import basemod.*;
import basemod.interfaces.PostInitializeSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

@SpireInitializer
public class SerializationMod implements PostInitializeSubscriber {

	private static final Logger logger = LogManager.getLogger(SerializationMod.class.getName());
	private static final String MODNAME = "Serialization Mod";
	private static final String AUTHOR = "Colin King";
	private static final String DESCRIPTION = "Serializes the state of Slay the Spire runs.";
	private static SpireConfig communicationConfig;
	private static final String VERBOSE_OPTION = "verbose";
	private static final boolean DEFAULT_VERBOSITY = false;

	public SerializationMod() {
		BaseMod.subscribe(this);
		try {
			Properties defaults = new Properties();
			defaults.put(VERBOSE_OPTION, Boolean.toString(DEFAULT_VERBOSITY));
			communicationConfig = new SpireConfig("SerializationMod", "config", defaults);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// TODO(colin): unused?
	public static void initialize() {
		SerializationMod mod = new SerializationMod();

	}

	public void receivePostInitialize() {
		setUpOptionsMenu();
	}

	private void setUpOptionsMenu() {
		ModPanel settingsPanel = new ModPanel();

		ModLabeledToggleButton verbosityOption = new ModLabeledToggleButton(
			"Show verbose log output",
			350, 500, Settings.CREAM_COLOR, FontHelper.charDescFont,
			getVerbosityOption(), settingsPanel, modLabel -> {
		},
			modToggleButton -> {
				if (communicationConfig != null) {
					communicationConfig.setBool(VERBOSE_OPTION, modToggleButton.enabled);
					try {
						communicationConfig.save();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		settingsPanel.addUIElement(verbosityOption);

		BaseMod.registerModBadge(ImageMaster.loadImage("Icon.png"), MODNAME, AUTHOR, DESCRIPTION, settingsPanel);
	}

	private static boolean getVerbosityOption() {
		if (communicationConfig == null) {
			return DEFAULT_VERBOSITY;
		}
		return communicationConfig.getBool(VERBOSE_OPTION);
	}
}
