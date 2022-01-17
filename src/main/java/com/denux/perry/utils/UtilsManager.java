package com.denux.perry.utils;

import com.denux.perry.utils.discord_config.DiscordConfig;
import com.denux.perry.utils.discord_config.DiscordConfigImpl;
import net.dv8tion.jda.api.entities.Guild;

public class UtilsManager {

    public DiscordConfig getDiscordConfig(Guild guild) {
        return new DiscordConfigImpl(guild);
    }
}
