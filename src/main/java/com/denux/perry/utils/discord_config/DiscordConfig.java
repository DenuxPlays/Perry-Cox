package com.denux.perry.utils.discord_config;
import org.bson.Document;

public interface DiscordConfig {

    /*
    Get
     */

    /*
    Get
     */

    /*
    Misc
     */

    /**
     * Creates a new config for the guild.
     * @return Returns true if the config was created successfully.
     */
    boolean createConfig();

    /**
     * Gets you the config.
     * @return Returns tje bson document.
     */
    Document getConfig();

    /*
    Misc
     */
}
