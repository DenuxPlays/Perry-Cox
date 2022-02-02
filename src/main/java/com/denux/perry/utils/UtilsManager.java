package com.denux.perry.utils;

import com.denux.perry.utils.discord_config.DiscordConfig;
import com.denux.perry.utils.discord_config.DiscordConfigImpl;
import net.dv8tion.jda.api.entities.Guild;

public class UtilsManager {

    /**
     * Gets the guild config from the specific guild.
     * @param guild The guild you want the config from. {@link Guild}
     * @return Returns the guild config.
     */
    public DiscordConfig getDiscordConfig(Guild guild) {
        return new DiscordConfigImpl(guild);
    }
}
