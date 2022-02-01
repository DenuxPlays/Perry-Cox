package com.denux.perry.bot.commands;

import com.denux.perry.bot.commands.dao.GuildSlashCommand;
import com.denux.perry.utils.UtilsManager;
import com.denux.perry.utils.discord_config.DiscordConfig;
import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.bson.Document;

import java.io.*;

@Slf4j
public class Test extends GuildSlashCommand implements SlashCommandHandler {

    public Test() {
        this.commandData = Commands.slash("test", "Testing stuff that need to be tested.");
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply(false).queue();
        DiscordConfig discordConfig = new UtilsManager().getDiscordConfig(event.getGuild());
        Document doc = discordConfig.getConfig();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        doc.remove("_id");
        File tempFile;
        try {
            tempFile = File.createTempFile(event.getGuild().getId() + "-", ".json");

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tempFile));
            bufferedWriter.write(gson.toJson(doc));
            bufferedWriter.close();
        } catch (IOException exception) {
            event.getHook().sendMessage("Something went wrong!").queue();
            return;
        }

        File finalTempFile = tempFile;
        event.getHook().sendFile(tempFile).queue(x -> {
            finalTempFile.delete();});
    }
}
