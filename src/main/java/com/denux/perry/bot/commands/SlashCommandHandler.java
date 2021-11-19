package com.denux.perry.bot.commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public interface SlashCommandHandler {

    void execute(SlashCommandEvent event);
}
