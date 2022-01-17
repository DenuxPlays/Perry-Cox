package com.denux.perry.utils.discord_config;

import com.denux.perry.utils.database.connections.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.dv8tion.jda.api.entities.Guild;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class DiscordConfigImpl implements DiscordConfig {

    /**
     * The ID from the Discord guild.
     */
    private Guild guild;

    private final MongoClient mongoClient;

    public DiscordConfigImpl(Guild guild) {
        this.guild = guild;
        mongoClient = Mongo.mongoClient;
    }

    @Override
    public boolean createConfig() {
        //TODO add check if guild config exist.
        Document doc = new Document()
                .append("guildID", guild.getIdLong());

        MongoDatabase database = mongoClient.getDatabase("perryCox");
        MongoCollection<Document> collection = database.getCollection("discordConfigs");
        collection.insertOne(doc);
        return true;
    }

    @Override
    public Document getConfig() {
        MongoDatabase database = mongoClient.getDatabase("perryCox");
        MongoCollection<Document> collection = database.getCollection("discordConfigs");
        return collection.find(eq("guildID", guild.getIdLong())).first();
    }
}
