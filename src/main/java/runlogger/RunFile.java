package runlogger;

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
			// If this is a new run, ensure we have a fresh log.
			//
			// Generally the log should be empty, since it'll get moved out of the saves directory upon victory/death.
			// However, if the run wasn't completed (or was abandoned from the main menu), the log will still be there.
			// Since a ".run" file doesn't exist, we don't keep this run log around. Instead, we temporarily back it up.
			FileHandle src = Gdx.files.local(this.getPath());
			if (src.exists()) {
				src.moveTo(Gdx.files.local(this.getPath()+".backUp"));
			}
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

	public void move(String dst) {
		String src = this.getPath();
		RunLogger.logger.info("Moving run log: {} to {}", src, dst);
		Gdx.files.local(src).moveTo(Gdx.files.local(dst));
		// Clean up any backups that may exist.
		Gdx.files.local(src+".backUp").delete();
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
