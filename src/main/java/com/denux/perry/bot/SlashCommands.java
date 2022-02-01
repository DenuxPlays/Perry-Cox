package com.denux.perry.bot;

import com.denux.perry.bot.commands.SlashCommandHandler;
import com.denux.perry.bot.commands.dao.GuildSlashCommand;
import com.denux.perry.bot.commands.dao.GuildSlashSubCommand;
import com.denux.perry.bot.commands.dao.GuildSlashSubCommandGroup;
import com.denux.perry.bot.services.Constants;
import com.denux.perry.utils.database.connections.Mongo;
import com.denux.perry.utils.database.connections.Postgres;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

@Slf4j
public class SlashCommands extends ListenerAdapter {

    private HashMap<String, SlashCommandHandler> slashCommands;
    private HashMap<String, CommandPrivilege[]> slashPrivileges;

    void registerSlashCommands(@NotNull Guild guild) {

        this.slashCommands = new HashMap<>();
        this.slashPrivileges = new HashMap<>();

        CommandListUpdateAction updateAction = guild.updateCommands();

        Reflections cmds = new Reflections(Constants.COMMANDS_PACKAGE);
        Set<Class<? extends GuildSlashCommand>> classes = cmds.getSubTypesOf(GuildSlashCommand.class);

        for (var clazz : classes) {
            SlashCommandData cmdData = null;

            try {
                GuildSlashCommand instance;
                try { instance = clazz.getDeclaredConstructor(Guild.class).newInstance(guild);
                } catch (NoSuchMethodException nsm) { instance = clazz.getConstructor().newInstance(); }

                if (instance.getCommandData() == null) {
                    log.warn("Class {} is missing CommandData. It will be ignored.", clazz.getName());
                    continue;
                }
                if (instance.getCommandPrivileges() != null) {
                    slashPrivileges.put(instance.getCommandData().getName(),
                            instance.getCommandPrivileges());
                }
                cmdData = (SlashCommandData) instance.getCommandData();
                log.info("{}[{}]{} Added CommandData from Class {}",
                        Constants.TEXT_WHITE, guild.getName(),
                        Constants.TEXT_RESET, clazz.getSimpleName());

                if (instance.getSubCommandClasses() == null
                        && instance.getSubCommandGroupClasses() == null) {
                    slashCommands.put(instance.getCommandData().getName() + " " +
                            null + " " +
                            null, (SlashCommandHandler) instance);
                }
                if (instance.getSubCommandGroupClasses() != null) {
                    for (var subGroupClazz : instance.getSubCommandGroupClasses()) {
                        GuildSlashSubCommandGroup subGroupInstance = (GuildSlashSubCommandGroup) subGroupClazz.getDeclaredConstructor().newInstance();
                        if (subGroupInstance.getSubCommandGroupData() == null) {
                            log.warn("Class {} is missing SubCommandGroupData. It will be ignored.", subGroupClazz.getName());
                            continue;
                        }
                        log.info("\t{}[{}]{} Adding SubCommandGroupData from Class {}",
                                Constants.TEXT_WHITE, clazz.getSimpleName(),
                                Constants.TEXT_RESET, subGroupClazz.getSimpleName());

                        if (subGroupInstance.getSubCommandClasses() == null) {
                            log.warn("Class {} is missing SubCommandClasses. It will be ignored.", subGroupClazz.getName());
                            continue;
                        }

                        SubcommandGroupData subCmdGroupData = subGroupInstance.getSubCommandGroupData();

                        for (var subClazz : subGroupInstance.getSubCommandClasses()) {
                            GuildSlashSubCommand subInstance = (GuildSlashSubCommand) subClazz.getDeclaredConstructor().newInstance();
                            if (subInstance.getSubCommandData() == null) {
                                log.warn("Class {} is missing SubCommandData. It will be ignored.", subClazz.getName());
                                continue;
                            }
                            subCmdGroupData.addSubcommands(subInstance.getSubCommandData());

                            slashCommands.put(instance.getCommandData().getName() + " " +
                                    subGroupInstance.getSubCommandGroupData().getName() + " " +
                                    subInstance.getSubCommandData().getName(), (SlashCommandHandler) subInstance);

                            log.info("\t\t{}[{}]{} Added SubCommandData from Class {}",
                                    Constants.TEXT_WHITE, subGroupClazz.getSimpleName(), Constants.TEXT_RESET,
                                    subClazz.getSimpleName());
                        }
                        cmdData.addSubcommandGroups(subCmdGroupData);
                    }
                }

                if (instance.getSubCommandClasses() != null) {
                    for (var subClazz : instance.getSubCommandClasses()) {
                        GuildSlashSubCommand subInstance = (GuildSlashSubCommand) subClazz.getDeclaredConstructor().newInstance();
                        if (subInstance.getSubCommandData() == null) {
                            log.warn("Class {} is missing SubCommandData. It will be ignored.", subClazz.getName());
                        } else {
                            cmdData.addSubcommands(subInstance.getSubCommandData());

                            slashCommands.put(instance.getCommandData().getName() + " " +
                                            null + " " + subInstance.getSubCommandData().getName(),
                                    (SlashCommandHandler) subInstance);

                            log.info("\t{}[{}]{} Added SubCommandData from Class {}",
                                    Constants.TEXT_WHITE, clazz.getSimpleName(),
                                    Constants.TEXT_RESET, subClazz.getSimpleName());
                        }
                    }
                }
            } catch (Exception e) { e.printStackTrace(); }
            updateAction.addCommands(cmdData);
        }
        log.info("{}[{}]{} Queuing SlashCommands",
                Constants.TEXT_WHITE, guild.getName(), Constants.TEXT_RESET);
        updateAction.queue();
    }

    @Override
    public void onReady(ReadyEvent event) {
        //Connection to the MongoDB
        Mongo.connect();

        //Testing postgresql connection.
        try {
            Connection con = new Postgres().connect();
            con.close();
            log.info("Successfully tested PostgreSQL connection.");
        } catch (Exception exception) {
            log.error("Testing PostgreSQL connection failed.");
            exception.printStackTrace();
            System.exit(0);
        }

        //Adding commands to the guilds
        for (var guild : event.getJDA().getGuilds()) registerSlashCommands(guild);
        log.info("{}[*]{} Command update completed\n",
                Constants.TEXT_WHITE, Constants.TEXT_RESET);
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        Bot.asyncPool.submit(() -> {
            try {
                if (event.getChannelType() == ChannelType.PRIVATE) return;
                var command = slashCommands.get(event.getName() + " " + event.getSubcommandGroup() + " " + event.getSubcommandName());
                command.execute(event);
            } catch (Exception e) {
                var embed = new EmbedBuilder()
                        .setColor(Constants.EMBED_GRAY)
                        .setAuthor(e.getClass().getSimpleName(), null, Bot.jda.getSelfUser().getEffectiveAvatarUrl())
                        .setDescription("```" + e.getMessage() + "```")
                        .setTimestamp(new Date().toInstant())
                        .build();
                event.getHook().sendMessageEmbeds(embed).setEphemeral(true).queue();
                e.printStackTrace();
            }
        });
    }
}
