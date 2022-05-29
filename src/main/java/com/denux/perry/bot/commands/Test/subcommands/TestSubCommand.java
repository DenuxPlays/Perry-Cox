package com.denux.perry.bot.commands.Test.subcommands;

import com.denux.perry.bot.Bot;
import com.dynxsty.dih4jda.commands.interactions.slash_command.ISlashCommand;
import com.dynxsty.dih4jda.commands.interactions.slash_command.dao.Subcommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class TestSubCommand extends Subcommand implements ISlashCommand {

    public TestSubCommand() {
        setSubcommandData(new SubcommandData("testsubcommand", "your mom"));
    }

    @Override
    public void handleSlashCommandInteraction(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        slashCommandInteractionEvent.reply("fuck you").queue();
        try {
            Bot.getCommandHandler().registerInteractions();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
