package com.denux.perry.utils.database.connections;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.denux.perry.bot.properties.ConfigString;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

@Slf4j
public class Mongo {

    /**
     * A static reference to the MongoDB Client.
     * All other MongoClients are based on this one.
     */
    public static MongoClient mongoClient;

    /**
     * Establishes a connection to the MongoDB Database.
     */
    public static void connect() {

        //Logging | Gives an error if something goes wrong
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = loggerContext.getLogger("org.mongodb.driver");
        rootLogger.setLevel(Level.ERROR);

        //Connection to Database
        MongoClientURI uri = new MongoClientURI(new ConfigString("monogodb").getValue());
        mongoClient = new MongoClient(uri);
        log.info("Connected.");
    }
}
