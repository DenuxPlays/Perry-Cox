package com.denux.perry.utils.discord_config;

import org.bson.Document;

public interface DiscordConfig {

    /**
     * Creates a new config for the guild.
     * @return Returns true if the config was created successfully.
     */
    boolean createConfig();

    /**
     * Gets you the config.
     * @return The config of the specific guild.
     */
    Document getConfig();
}
