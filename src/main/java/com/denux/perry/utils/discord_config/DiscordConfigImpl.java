package com.denux.perry.utils.discord_config;

import com.denux.perry.utils.database.connections.Mongo;
import com.google.gson.JsonObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.dv8tion.jda.api.entities.Guild;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;;

public class DiscordConfigImpl implements DiscordConfig {

    /**
     * The ID from the Discord guild.
     */
    private Guild guild;

    /**
     * A reference to the MongoDB.
     * Equal with {@link Mongo#mongoClient}
     */
    private final MongoClient mongoClient;

    public DiscordConfigImpl(Guild guild) {
        this.guild = guild;
        mongoClient = Mongo.mongoClient;
    }

    /**
     * Checks if there is already a config in the database.
     * @return Returns true if there is a config.
     */
    private boolean checkIfConfigExists() {
        MongoDatabase database = mongoClient.getDatabase("perryCox");
        MongoCollection<Document> collection = database.getCollection("discordConfigs");
        Document doc = collection.find(eq("guildID", guild.getIdLong())).first();
        return doc != null;
    }

    @Override
    public boolean createConfig() {
        if (checkIfConfigExists()) {
            return false;
        } else {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("logChannel", 0L);
            jsonObject.addProperty("publicLogChannel", 0L);
            jsonObject.addProperty("welcomeChannel", 0L);
            Document doc = new Document()
                    .append("guildID", guild.getIdLong())
                    .append("channels", Document.parse(jsonObject.toString()));

            MongoDatabase database = mongoClient.getDatabase("perryCox");
            MongoCollection<Document> collection = database.getCollection("discordConfigs");
            collection.insertOne(doc);
        }
        return true;
    }

    @Override
    public Document getConfig() {
        if (!checkIfConfigExists()) createConfig();
        MongoDatabase database = mongoClient.getDatabase("perryCox");
        MongoCollection<Document> collection = database.getCollection("discordConfigs");
        return collection.find(eq("guildID", guild.getIdLong())).first();
    }
}
