package dev.denux.perrycox.bot.commands;

import dev.denux.perrycox.util.EmbedUtil;
import dev.denux.perrycox.util.WebhookUtil;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import xyz.dynxsty.dih4jda.interactions.commands.application.SlashCommand;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.regex.Pattern;

public class HyperlinkCommand extends SlashCommand {

	private static final Pattern URL_PATTERN = Pattern.compile("^https?://(?:www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b[-a-zA-Z0-9()@:%_+.~#?&/=]*$");

	public HyperlinkCommand() {
		this.setCommandData(Commands.slash("hyperlink", "Creates a hyperlink")
				.addOption(OptionType.STRING, "text", "The text of the hyperlink", true)
				.addOption(OptionType.STRING, "url", "The url of the hyperlink", true));
	}

	@Override
	public void execute(@Nonnull SlashCommandInteractionEvent event) {
		event.deferReply(true).queue();
		String text = getOption(event, "text");
		String url = getOption(event, "url");
		if (!URL_PATTERN.matcher(url).matches()) {
			event.getHook().sendMessage("The url is not valid").queue();
			return;
		}

		WebhookUtil.webhookByChannel(event.getChannel().asTextChannel(),
				webhook -> WebhookUtil.mirrorEmbedToWebhook(webhook, event.getMember(), List.of(EmbedUtil.hyperlinkEmbed(text, url)), List.of()));
		event.getHook().sendMessage("Hyperlink created").queue();
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
