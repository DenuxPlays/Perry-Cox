package com.denux.perry.bot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface SlashCommandHandler {

    void execute(SlashCommandInteractionEvent event);
}
