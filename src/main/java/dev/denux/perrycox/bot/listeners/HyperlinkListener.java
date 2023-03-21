package dev.denux.perrycox.bot.listeners;

import dev.denux.perrycox.util.EmbedUtil;
import dev.denux.perrycox.util.WebhookUtil;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.regex.Pattern;

@Slf4j
public class HyperlinkListener extends ListenerAdapter {

	private final static Pattern HYPERLINK_REGEX = Pattern.compile("\\[(?<text>[^]]*)]\\((?<link>[^)]*)\\)");

	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) return;
		if (event.getMember() == null) return;

		String text = event.getMessage().getContentRaw();
		if (HYPERLINK_REGEX.matcher(text).find()) {
			event.getMessage().delete().queue();
			WebhookUtil.webhookByChannel(event.getChannel().asTextChannel(),
					webhook -> WebhookUtil.mirrorEmbedToWebhook(webhook, event.getMember(), List.of(EmbedUtil.buildCleanEmbed(text)),
							event.getMessage().getAttachments()));
		}
	}
}
