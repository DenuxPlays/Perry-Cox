package dev.denux.perrycox.bot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import xyz.dynxsty.dih4jda.interactions.commands.application.SlashCommand;

import javax.annotation.Nonnull;
import java.util.regex.Pattern;

public class Hyperlink extends SlashCommand {

	private static final Pattern URL_PATTERN = Pattern.compile("^https?://(?:www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b[-a-zA-Z0-9()@:%_+.~#?&/=]*$");

	public Hyperlink() {
		this.setCommandData(Commands.slash("hyperlink", "Creates a hyperlink")
				.addOption(OptionType.STRING, "text", "The text of the hyperlink", true)
				.addOption(OptionType.STRING, "url", "The url of the hyperlink", true));
	}

	@Override
	public void execute(@Nonnull SlashCommandInteractionEvent event) {
		event.deferReply(false).queue();
		String text = getOption(event, "text");
		String url = getOption(event, "url");
		if (!URL_PATTERN.matcher(url).matches()) {
			event.getHook().sendMessage("The url is not valid").queue();
			return;
		}

		MessageEmbed embed = new EmbedBuilder()
				.setDescription("[" + text + "](" + url + ")")
				.build();
		event.getHook().sendMessageEmbeds(embed).queue();
	}

	@Nonnull
	private String getOption(@Nonnull SlashCommandInteractionEvent event, @Nonnull String name) {
		OptionMapping option = event.getOption(name);
		if (option == null) {
			event.getHook().sendMessage("You must provide the " + name + " of the hyperlink").queue();
			throw new IllegalArgumentException("You must provide the " + name + " of the hyperlink");
		}
		return option.getAsString();
	}
}
