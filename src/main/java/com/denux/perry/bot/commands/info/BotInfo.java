package com.denux.perry.bot.commands.info;

import com.denux.perry.bot.Bot;
import com.denux.perry.bot.commands.SlashCommandHandler;
import com.denux.perry.bot.commands.dao.GuildSlashCommand;
import com.denux.perry.bot.services.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

public class BotInfo extends GuildSlashCommand implements SlashCommandHandler {

    public BotInfo() {
        this.commandData = new CommandData("botinfo", "Gives you the general information about the bot.");
    }

    @Override
    public void execute(@NotNull SlashCommandEvent event) {

        //Makes the "Slashy is thinking" and when its true its only visible for the User who sent the command
        event.deferReply().setEphemeral(true).queue();

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