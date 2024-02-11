package serializationmod;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;

import java.util.ArrayList;
import java.util.List;

/**
 * RunFile synchronizes reads/writes to a file storing the actions and state updates for a run.
 *
 * Logs for active runs are co-located with the save file. Once a run completes, the log
 * is moved into the runs folder to be co-located with the `.run` file.
 */
public class RunFile {
	public int numLines = 0;

	private final AbstractPlayer.PlayerClass character;
	private final List<String> buffer;

	public RunFile(AbstractPlayer.PlayerClass character, boolean isContinue) {
		this.character = character;
		this.numLines = 0;
		this.buffer = new ArrayList<>();

		if (!isContinue) {
			// If this is a new run, ensure the logs are cleared.
			//
			// This can happen if a user abandons a run from the main menu, since a ".run" file is never created in that
			// case (and therefore, we don't know what to name the run...so we just delete it). TIL this also means
			// those runs don't show up in your run history!
			//
			// There are other edge cases where this could also happen (e.g. a user finishes a run without Serialization
			// Mod).
			Gdx.files.local(this.getPath()).delete();
		}
	}

	public void append(String msg) {
		this.numLines += 1;
		this.buffer.add(msg);
	}

	public void flush() {
		if (this.buffer.isEmpty()) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		for (String line : this.buffer) {
			sb.append(line);
			sb.append("\n");
		}

		this.buffer.clear();

		FileHandle file = Gdx.files.local(this.getPath());
		file.writeString(sb.toString(), true);
	}

	public String getPath() {
		// This logic is based on SaveAndContinue.getPlayerSavePath
		StringBuilder sb = new StringBuilder();
		sb.append(SaveAndContinue.SAVE_PATH);
		if (CardCrawlGame.saveSlot != 0) {
			sb.append(CardCrawlGame.saveSlot).append("_");
		}
		sb.append(this.character.name());
		sb.append(".run.log");
		return sb.toString();
	}
}
