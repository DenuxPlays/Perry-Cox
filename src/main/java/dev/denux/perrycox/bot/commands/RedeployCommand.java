package dev.denux.perrycox.bot.commands;

import dev.denux.perrycox.bot.Bot;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.utils.FileUpload;
import xyz.dynxsty.dih4jda.interactions.commands.application.SlashCommand;

import java.io.IOException;

@Slf4j
public class RedeployCommand extends SlashCommand {

	public RedeployCommand() {
		setCommandData(Commands.slash("redeploy", "Restarts and redeploys the bot.")
				.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)));
		setRequiredPermissions(Permission.ADMINISTRATOR);
		setRequiredUsers(313671802809352194L);
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		event.deferReply(true).queue();
		log.warn("Redeploying... Requested by: " + event.getUser().getAsTag());
		event.getHook().sendMessage("Redeploying, this may take a few minutes.").queue();
		try {
			Process process = new ProcessBuilder("/bin/sh", Bot.getBotConfig().getRedeployScriptLocation())
					.start();
			process.waitFor();
			String error = new String(process.getErrorStream().readAllBytes());
			String result = new String(process.getInputStream().readAllBytes());
			if (!error.isBlank() || !error.isEmpty()) {
				log.error(error);
			}
			log.info("Result from redeploy script: {}", result);
			if (result.contains("FAILED TO COMPILE")) {
				event.getHook().sendMessage("Compilation failed, redeploy canceled.").queue();
				event.getHook().sendFiles(FileUpload.fromData(error.getBytes(), "error.txt")).queue();
				log.warn("Redeploy canceled due to compilation error.");
				return;
			} else {
				log.info("Compiled successfully, stopping the bot.");
				event.getHook().sendMessage("Compilation successful, restarting...").complete();
			}
		} catch (InterruptedException | IOException exception) {
			throw new RuntimeException(exception);
		}
		event.getJDA().shutdown();
		System.exit(0);
	}
}
