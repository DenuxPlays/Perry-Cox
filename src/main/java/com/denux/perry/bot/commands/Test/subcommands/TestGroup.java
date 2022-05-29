package com.denux.perry.bot.commands.Test.subcommands;

import com.dynxsty.dih4jda.commands.interactions.slash_command.dao.SubcommandGroup;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

public class TestGroup extends SubcommandGroup {

    public TestGroup() {
        setSubcommandGroupData(new SubcommandGroupData("testgroup", "testing group"));
        setSubcommands(TestSubCommand.class);
    }
}
