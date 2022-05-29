package com.denux.perry.bot.commands.Test;

import com.denux.perry.bot.commands.Test.subcommands.TestGroup;
import com.dynxsty.dih4jda.commands.interactions.slash_command.ISlashCommand;
import com.dynxsty.dih4jda.commands.interactions.slash_command.dao.GuildSlashCommand;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class Test extends GuildSlashCommand {

    public Test() {
        this.setCommandData(Commands.slash("test", "Testing stuff"));
        this.setSubcommandGroups(TestGroup.class);
    }

}
