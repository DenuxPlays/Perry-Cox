package dev.denux.perrycox.bot.listeners;

import dev.denux.perrycox.bot.Constants;
import dev.denux.perrycox.util.WebhookUtil;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.regex.Pattern;

public class HyperlinkListener extends ListenerAdapter {

	private final static Pattern HYPERLINK_REGEX = Pattern.compile("\\[(?<text>[^]]*)]\\((?<link>[^)]*)\\)");

	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		if (event.getAuthor().isBot() || event.getAuthor().isSystem()) return;
		if (event.getMember() == null) return;
		if (event.getChannel().getIdLong() == Constants.TEST_CHANNEL) return;

		String text = event.getMessage().getContentRaw();
		if (HYPERLINK_REGEX.matcher(text).find()) {
			WebhookUtil.webhookByChannel(event.getChannel().asTextChannel(),
					webhook -> WebhookUtil.mirrorMessageToWebhook(webhook, event.getMessage(), List.of(),
							event.getMessage().getAttachments()));
			event.getMessage().delete().queue();
		}
	}
}
