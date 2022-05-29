package com.denux.perry.bot.commands;

import com.denux.perry.bot.Bot;
import com.dynxsty.dih4jda.commands.interactions.context_command.IMessageContextCommand;
import com.dynxsty.dih4jda.commands.interactions.context_command.IUserContextCommand;
import com.dynxsty.dih4jda.commands.interactions.context_command.dao.GuildContextCommand;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class PingContextMenu extends GuildContextCommand implements IMessageContextCommand {

    public PingContextMenu() {
        setCommandData(Commands.message("Ping"));
    }

    @Override
    public void handleMessageContextInteraction(MessageContextInteractionEvent userContextInteractionEvent) {
        userContextInteractionEvent.reply("Your mom").queue();
    }
}
