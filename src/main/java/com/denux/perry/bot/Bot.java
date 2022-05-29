package com.denux.perry.bot;

import com.denux.perry.bot.properties.ConfigString;
import com.denux.perry.bot.system.SystemSetup;
import com.dynxsty.dih4jda.DIH4JDA;
import com.dynxsty.dih4jda.DIH4JDABuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ScheduledExecutorService;

@Slf4j
public class Bot extends ListenerAdapter {
    /**
     * A general-purpose thread pool that can be used by the bot to execute
     * tasks outside the main event processing thread.
     */
    @Getter
    @Setter
    private static ScheduledExecutorService asyncPool;

    @Getter
    private static DIH4JDA commandHandler;
    /**
     * A static reference to the JDA Instance.
     */
    @Getter
    private static JDA jda;

    /**
     * The main method that starts the bot.
     * @param args Command-line arguments.
     * @throws Exception If any exception occurs during bot creation.
     */
    public static void main(String[] args) throws Exception {
        SystemSetup.init();

        //Creating the bot instance
        jda = JDABuilder.createDefault(new ConfigString("token").getValue())
                .enableIntents(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
                .enableCache(CacheFlag.ACTIVITY)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .addEventListeners(new Bot())
                .build();
        addEventListeners(jda);

        commandHandler = DIH4JDABuilder
                .setJDA(jda)
                .setCommandsPackage(Constants.COMMANDS_PACKAGE)
                .setOwnerId(Constants.OWNER_ID)
                .build();
    }

    private static void addEventListeners(JDA jda) {
        jda.addEventListener();
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
    }
}
