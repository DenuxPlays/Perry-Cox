package dev.denux.perrycox.bot.commands;

import dev.denux.perrycox.bot.Bot;
import dev.denux.perrycox.bot.Constants;
import dev.denux.perrycox.util.SystemUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import xyz.dynxsty.dih4jda.interactions.commands.application.RegistrationType;
import xyz.dynxsty.dih4jda.interactions.commands.application.SlashCommand;

import java.time.Instant;

public class BotInfo extends SlashCommand {

	public BotInfo() {
		setCommandData(Commands.slash("botinfo", "Gives you the general information about the Bot."));
		setRegistrationType(RegistrationType.GLOBAL);
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		event.deferReply(true).queue();

		var embed = new EmbedBuilder()
				.setTitle("Botinfo")
				.setColor(Constants.EMBED_GRAY)
				.setTimestamp(Instant.now())
				.setThumbnail(Bot.getJda().getSelfUser().getEffectiveAvatarUrl())
				.addField("Name", format(Bot.getJda().getSelfUser().getAsTag()), false)
				.addField("Library", format("JDA (Java)"), false)
				.addField("Server Count", format(String.valueOf(Bot.getJda().getGuilds().size())), false)
				.addField("User Count", format(String.valueOf(Bot.getJda().getUsers().size())), false)
				.addField("Bot latency", format(Bot.getJda().getGatewayPing() + "ms"), false)
				.addField("Git commit", format(Constants.GIT_COMMIT), false)
				.addField("Runtime", format(SystemUtil.getOperationSystem() + ", Java " + Runtime.version()), false)
				.addField("Hostname", format(SystemUtil.getHostname()), false)
				.setFooter(event.getMember().getUser().getAsTag(), event.getMember().getUser().getEffectiveAvatarUrl())
				.build();

		event.getHook().sendMessageEmbeds(embed).queue();
	}

	private static String format(String info) {
		return "`" + info + "`";
	}
}
