package codedcosmos.cometbot.guild.commands;

import codedcosmos.cometbot.core.CometBot;
import codedcosmos.cometbot.guild.context.CometGuildContext;
import codedcosmos.hyperdiscord.chat.TextSender;
import codedcosmos.hyperdiscord.command.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class GlobalStats implements Command {
	
	private static long lastSaveTime = System.currentTimeMillis();
	
	@Override
	public String getName() {
		return "globalstats";
	}
	
	@Override
	public String getHelp() {
		return "Shows the global stats for cometbot";
	}
	
	@Override
	public String[] getStynax() {
		return new String[] {""};
	}
	
	@Override
	public void run(MessageReceivedEvent event) throws Exception {
		int tracksQueued = 0;
		int tracksPlayed = 0;
		int likes = 0;
		int commandsEntered = 0;
		int goodbots = 0;
		
		ArrayList<CometGuildContext> guilds = CometBot.guilds.getGuilds();
		for (CometGuildContext context : guilds) {
			tracksQueued += context.getStatsRecorder().getTracksQueued();
			tracksPlayed += context.getStatsRecorder().getTracksPlayed();
			likes += context.getStatsRecorder().getLikes();
			commandsEntered += context.getStatsRecorder().getCommandsEntered();
			goodbots += context.getStatsRecorder().getGoodbots();
		}
		
		MessageBuilder builder = new MessageBuilder();
		EmbedBuilder embedBuilder = new EmbedBuilder();
		
		embedBuilder.setTitle("Global Stats for CometBot");
		embedBuilder.setFooter("Stats only recorded since CometBot v2.0 currently at v" + CometBot.VERSION);
		embedBuilder.setThumbnail(CometBot.AVATAR_URL);
		
		String description = "";
		
		description += "Tracks Queued---------: " + tracksQueued + "\n";
		description += "Tracks Played ----------: " + tracksPlayed + "\n";
		description += "Likes --------------------: " + likes + "\n";
		description += "Commands Entered ---: " + commandsEntered + "\n";
		description += "Goodbots ---------------: " + goodbots + "\n";
		
		description += "\n";
		
		description += "Num Guilds ------------: " + guilds.size() + "\n";
		
		embedBuilder.setDescription(description);
		
		Message message = builder.setEmbed(embedBuilder.build()).build();
		TextSender.send(event, message);
	}
	
	public String[] getAliases() {
		return new String[] {"globalstatz"};
	}
}