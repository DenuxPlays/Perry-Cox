package com.denux.perry.bot;

import com.denux.perry.bot.properties.ConfigString;
import com.denux.perry.bot.services.Constants;
import com.denux.perry.utils.database.connections.Postgres;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Bot {
    /**
     * A general-purpose thread pool that can be used by the bot to execute
     * tasks outside the main event processing thread.
     */
    public static ScheduledExecutorService asyncPool;

    /**
     * A static reference to the JDA Instance.
     */
    public static JDA jda;

    /**
     * The main method that starts the bot.
     * @param args Command-line arguments.
     * @throws Exception If any exception occurs during bot creation.
     */
    public static void main(String[] args) throws Exception {

        //Part 1 for async commands
        asyncPool = Executors.newScheduledThreadPool(Constants.THREAD_POOL_SIZE);

        //Other stuff
        //Setting connection String for the PostgreSQL database.
        Postgres.connectionString = new ConfigString("postgresql").getValue();

        //Creating the bot instance
        jda = JDABuilder.createDefault(new ConfigString("token").getValue())
                .enableIntents(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
                .enableCache(CacheFlag.ACTIVITY)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .build();
        addEventListeners(jda);
    }

    private static void addEventListeners(JDA jda) {
        jda.addEventListener(
                new SlashCommands()
        );
    }
}
