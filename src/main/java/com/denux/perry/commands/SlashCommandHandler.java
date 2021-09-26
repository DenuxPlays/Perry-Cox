package com.denux.perry.commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public interface SlashCommandHandler {

    void execute(SlashCommandEvent event);
}
