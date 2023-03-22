package dev.denux.perrycox.util;

import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.external.JDAWebhookClient;
import club.minnced.discord.webhook.send.AllowedMentions;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class WebhookUtil {

	private static final String STANDARD_WEBHOOK_NAME = "PerryCox-webhook";

	private WebhookUtil() {}

	public static void webhookByChannel(@Nonnull TextChannel channel, @Nonnull Consumer<? super Webhook> callback) {
		channel.retrieveWebhooks().queue(webhooks -> {
			Optional<Webhook> hook = webhooks.stream()
					.filter(webhook -> webhook.getName().equals(STANDARD_WEBHOOK_NAME))
					.filter(webhook -> webhook.getToken() != null).findAny();
			if (hook.isPresent()) {
				callback.accept(hook.get());
			} else {
				channel.createWebhook(STANDARD_WEBHOOK_NAME).queue(callback);
			}
		});
	}

	public static void mirrorMessageToWebhook(@Nonnull Webhook webhook, @Nonnull Message original,
											  @Nonnull List<MessageEmbed> embeds, @Nonnull List<Message.Attachment> attachments) {
		mirrorMessageToWebhook(webhook, original, embeds, attachments, 0);
	}

	public static void mirrorMessageToWebhook(@Nonnull Webhook webhook, @Nonnull Message original, @Nonnull List<MessageEmbed> embeds,
											  @Nonnull List<Message.Attachment> attachments, long threadId) {
		JDAWebhookClient client = new WebhookClientBuilder(webhook.getIdLong(), webhook.getToken())
				.setThreadId(threadId).buildJDA();
		WebhookMessageBuilder message = new WebhookMessageBuilder()
				.setAllowedMentions(AllowedMentions.none())
				.setContent(original.getContentRaw())
				.setAvatarUrl(original.getMember().getEffectiveAvatarUrl())
				.setUsername(original.getMember().getEffectiveName());
		if (!embeds.isEmpty()) {
			message.addEmbeds(embeds.stream().map(e -> WebhookEmbedBuilder.fromJDA(e).build()).toList());
		}
		List<CompletableFuture<?>> futures = new ArrayList<>();
		for (Message.Attachment attachment : attachments) {
			futures.add(attachment.getProxy().download().thenAccept(is -> message.addFile((attachment.isSpoiler() ? "SPOILER_" : "") + attachment.getFileName(), is)));
		}
		CompletableFuture.allOf(futures.toArray(new CompletableFuture[attachments.size()])).thenCompose(v -> client.send(message.build()))
				.whenComplete((result, err) -> client.close());
	}
}
