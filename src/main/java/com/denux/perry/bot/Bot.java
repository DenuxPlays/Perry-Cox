package com.denux.perry.bot;

import com.denux.perry.bot.properties.ConfigString;
import com.denux.perry.bot.services.Constants;
import com.denux.perry.utils.database.connections.Mongo;
import com.denux.perry.utils.database.connections.Postgres;
import com.dynxsty.dih4jda.DIH4JDA;
import com.dynxsty.dih4jda.DIH4JDABuilder;
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

import java.sql.Connection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
public class Bot extends ListenerAdapter {
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
                .addEventListeners(new Bot())
                .build();
        addEventListeners(jda);

        DIH4JDA commandHandler = DIH4JDABuilder
                .setJDA(jda)
                .setCommandsPackage("com.denux.perry.bot.commands")
                .setOwnerId(313671802809352194L)
                .build();
    }

    private static void addEventListeners(JDA jda) {
        jda.addEventListener(
        );
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
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
    }
}
