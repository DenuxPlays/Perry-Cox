package com.denux.perry.bot.commands.info;

import com.denux.perry.bot.Bot;
import com.denux.perry.bot.services.Constants;
import dev.denux.sch4jda.slash_command.ISlashCommand;
import dev.denux.sch4jda.slash_command.dto.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.time.Instant;

public class BotInfo extends SlashCommand implements ISlashCommand {

    public BotInfo() {
        this.setCommandData(Commands.slash("botinfo", "Infos about the Bot."));
    }

    @Override
    public void handleSlash(SlashCommandInteractionEvent event) {
        var embed = new EmbedBuilder()
                .setTitle("Botinfo")
                .setColor(Constants.EMBED_GRAY)
                .setTimestamp(Instant.now())
                .setThumbnail(Bot.jda.getSelfUser().getEffectiveAvatarUrl())
                .addField("Name","`" + Bot.jda.getSelfUser().getAsTag() + "`", false)
                .addField("Library", "`JDA (Java)`", false)
                .addField("Server Count", "`" + Bot.jda.getGuilds().size() + "`", false)
                .addField("User Count", "`" + Bot.jda.getUsers().size() + "`", false)
                .addField("Version", "`" + Constants.VERSION + "`", true)
                .addField("Bot Owner", "`" + event.getJDA().retrieveUserById(Constants.OWNER_ID).complete().getAsTag() + "`", false)
                .setFooter(event.getMember().getUser().getAsTag(), event.getMember().getUser().getEffectiveAvatarUrl())
                .build();

        event.getHook().sendMessageEmbeds(embed).queue();

    }
}