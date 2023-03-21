package dev.denux.perrycox.util;

import dev.denux.perrycox.bot.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import javax.annotation.Nonnull;

public class EmbedUtil {

	private EmbedUtil() {}

	@Nonnull
	public static MessageEmbed buildCleanEmbed(@Nonnull String text) {
		return new EmbedBuilder()
				.setColor(Constants.EMBED_GRAY)
				.setDescription(text)
				.build();
	}

	@Nonnull
	public static MessageEmbed hyperlinkEmbed(@Nonnull String text, @Nonnull String link) {
		return buildCleanEmbed(String.format("[%s](%s)", text, link));
	}
}
