package com.denux.perry.utils.discord_config;

import net.dv8tion.jda.api.entities.Guild;
import org.bson.Document;

public interface DiscordConfig {

    boolean createConfig();

    Document getConfig();
}
