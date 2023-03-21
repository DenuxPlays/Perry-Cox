package dev.denux.perrycox.bot;

import dev.denux.perrycox.bot.listeners.HyperlinkListener;
import dev.denux.perrycox.bot.system.BotConfig;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import xyz.dynxsty.dih4jda.DIH4JDA;
import xyz.dynxsty.dih4jda.DIH4JDABuilder;

import javax.annotation.Nonnull;


@Slf4j
public class Bot {

    /**
     * A static reference to the {@link BotConfig} instance.
     */
    @Getter
    private static BotConfig botConfig;
    /**
     * A static reference to the {@link DIH4JDA} instance.
     */
    @Getter
    private static DIH4JDA commandHandler;
    /**
     * A static reference to the {@link JDA} Instance.
     */
    @Getter
    private static JDA jda;

    /**
     * The main method that starts the bot.
     * @param args Command-line arguments.
     * @throws Exception If any exception occurs during bot creation.
     */
    public static void main(String[] args) throws Exception {
        BotConfig tmp = BotConfig.init();
        if (tmp == null) {
            log.error("BotConfig is null");
            return;
        }
        botConfig = tmp;

        //Creating the bot instance
        jda = JDABuilder.createDefault(botConfig.getBotToken())
                .enableIntents(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
                .enableCache(CacheFlag.ACTIVITY)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .build();
        addEventListeners(jda);

        commandHandler = DIH4JDABuilder
                .setJDA(jda)
                .setCommandPackages(Constants.COMMANDS_PACKAGE)
                .build();
    }

    private static void addEventListeners(@Nonnull JDA jda) {
        jda.addEventListener(new HyperlinkListener());
    }
}
