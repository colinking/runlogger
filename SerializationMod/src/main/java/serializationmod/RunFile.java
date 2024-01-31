package serializationmod;

import com.megacrit.cardcrawl.characters.AbstractPlayer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * RunFile synchronizes reads/writes to a file storing the actions and state updates for a run.
 */
public class RunFile {
	private final Long seed;
	private final String fileName;
	private final AbstractPlayer.PlayerClass character;

	public RunFile(Long seed, AbstractPlayer.PlayerClass character) {
		this.seed = seed;
		this.fileName = Long.toString(System.currentTimeMillis() / 1000L);
		this.character = character;
	}

	private java.nio.file.Path getPath() {
		String name = character.name().toLowerCase();
		return Paths.get("/users/colin/Downloads/"+ name + "_" + this.fileName + ".json");
	}

	public void clear() {
		try {
			Files.write(this.getPath(), new byte[0], StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void append(String msg) {
		msg += "\n";
		try {
			Files.write(this.getPath(), msg.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
