package dev.denux.perrycox.bot.system;

import dev.denux.dtp.DTP;
import dev.denux.perrycox.bot.Constants;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.File;
import java.io.Flushable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Getter
@Slf4j
@SuppressWarnings("FieldMayBeFinal")
public class BotConfig implements Flushable {

	/**
	 * Loads the config file or if no one exists it creates a new config + the file.
	 * @return A {@link BotConfig} instance or null if there was an error.
	 */
	@Nullable
	public static BotConfig init()  {
		Path configFilePath = Path.of(Constants.BOT_CONFIG_FILE);
		//Checking if BotConfig.toml exists
		if (Files.exists(configFilePath)) {
			log.debug("Trying to load the bot config file.");
			try(BufferedReader reader = Files.newBufferedReader(configFilePath)) {
				BotConfig config = new DTP().fromToml(reader, BotConfig.class);
				if (config == null) {
					log.error("Could not init bot config.");
					return null;
				}
				config.flush();
				log.debug("Successfully loaded the bot config file.");
				return config;
			} catch (IOException exception) {
				throw new UncheckedIOException(exception);
			}
		}
		//Create a "new" bot config and flush it to the disk if no file existed.
		else {
			log.debug("Loading bot config file from the disk.");
			BotConfig config = new BotConfig();
			try {
				config.flush();
			} catch (IOException exception) {
				throw new UncheckedIOException(exception);
			}
			return config;
		}
	}

	/**
	 * Flushes the BotConfig to the disk.
	 * @throws IOException if they file could not be written.
	 */
	@Override
	public void flush() throws IOException {
		log.debug("Flushing BotConfig to the disk.");
		File file = new File(Constants.BOT_CONFIG_FILE);
		file.createNewFile();
		new DTP().writeTomlToFile(this, file);
	}

	private String botToken = "Your Bot Token!";
	private String redeployScriptLocation = "redeploy.sh";
}
