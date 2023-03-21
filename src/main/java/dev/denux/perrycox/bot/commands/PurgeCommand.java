package dev.denux.perrycox.bot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;
import xyz.dynxsty.dih4jda.interactions.commands.application.RegistrationType;
import xyz.dynxsty.dih4jda.interactions.commands.application.SlashCommand;

public class PurgeCommand extends SlashCommand {

	public PurgeCommand() {
		setCommandData(Commands.slash("purge", "Purges the given amount of messages.")
				.addOption(OptionType.INTEGER, "amount", "The amount of messages to purge.", true)
				.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_MANAGE)));
		setRegistrationType(RegistrationType.GLOBAL);
		setRequiredPermissions(Permission.MESSAGE_MANAGE);
	}

	@Override
	public void execute(@NotNull SlashCommandInteractionEvent event) {
		event.deferReply(true).queue();
		int amount = event.getOption("amount", OptionMapping::getAsInt);
		event.getChannel().asTextChannel().getHistory()
				.retrievePast(amount)
				.queue(messages -> event.getChannel().purgeMessages(messages));
		event.getHook().sendMessage("Purging " + amount + " messages.").queue();
	}
}