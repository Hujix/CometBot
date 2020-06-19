package codedcosmos.cometbot.guild.commands;

import codedcosmos.cometbot.core.CometBot;
import codedcosmos.cometbot.guild.context.CometGuildContext;
import codedcosmos.cometbot.guild.context.sub.StatsRecorder;
import codedcosmos.hyperdiscord.chat.TextSender;
import codedcosmos.hyperdiscord.command.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Stats implements Command {
	
	private static long lastSaveTime = System.currentTimeMillis();
	
	@Override
	public String getName() {
		return "stats";
	}
	
	@Override
	public String getHelp() {
		return "Shows the stats for the current guild";
	}
	
	@Override
	public String[] getStynax() {
		return new String[] {""};
	}
	
	@Override
	public void run(MessageReceivedEvent event) throws Exception {
		CometGuildContext context = CometBot.guilds.getContextBy(event.getGuild());
		StatsRecorder recorder = context.getStatsRecorder();
		
		MessageBuilder builder = new MessageBuilder();
		EmbedBuilder embedBuilder = new EmbedBuilder();
		
		embedBuilder.setTitle("Stats for " + context.getGuild().getName());
		embedBuilder.setFooter("Stats only recorded since CometBot v2.0 currently at v" + CometBot.VERSION);
		embedBuilder.setThumbnail(context.getGuild().getIconUrl());
		
		String description = "";
		
		description += "Tracks Queued---------: " + recorder.getTracksQueued() + "\n";
		description += "Tracks Played ----------: " + recorder.getTracksPlayed() + "\n";
		description += "Likes --------------------: " + recorder.getLikes() + "\n";
		description += "Commands Entered ---: " + recorder.getCommandsEntered() + "\n";
		description += "Goodbots ---------------: " + recorder.getGoodbots() + "\n";
		
		embedBuilder.setDescription(description);
		
		Message message = builder.setEmbed(embedBuilder.build()).build();
		TextSender.send(event, message);
	}
	
	public String[] getAliases() {
		return new String[] {"statz", "stat", "howmanygoodbots"};
	}
}
