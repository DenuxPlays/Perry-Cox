package com.denux.perry.bot.system;

import com.denux.perry.bot.Bot;
import com.denux.perry.bot.Constants;
import com.denux.perry.bot.properties.ConfigString;
import com.denux.perry.utils.database.connections.Mongo;
import com.denux.perry.utils.database.connections.Postgres;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.util.concurrent.Executors;

@Slf4j
public class SystemSetup {

    public static void init() {
        log.info("System-Initializing (1/2)");
        //Part 1 for async commands
        Bot.setAsyncPool(Executors.newScheduledThreadPool(Constants.THREAD_POOL_SIZE));

        //Other stuff
        //Setting connection String for the PostgreSQL database.
        Postgres.connectionString = new ConfigString("postgresql").getValue();
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
