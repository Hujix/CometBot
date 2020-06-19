package codedcosmos.cometbot.guild.commands;

import codedcosmos.cometbot.core.CometBot;
import codedcosmos.cometbot.guild.context.CometGuildContext;
import codedcosmos.hyperdiscord.chat.TextSender;
import codedcosmos.hyperdiscord.command.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CurrentSettings implements Command {
	@Override
	public String getName() {
		return "currentsettings";
	}
	
	@Override
	public String getHelp() {
		return "Shows the current configured settings";
	}
	
	@Override
	public String[] getStynax() {
		return new String[] {""};
	}
	
	@Override
	public void run(MessageReceivedEvent event) throws Exception {
		// Context
		CometGuildContext context = CometBot.guilds.getContextBy(event);
		
		// Build Message
		MessageBuilder builder = new MessageBuilder();
		EmbedBuilder embedBuilder = new EmbedBuilder();
		
		embedBuilder.setTitle("Settings");
		embedBuilder.setFooter("CometBot v" + CometBot.VERSION);
		
		String description = "";
		
		description += "Shuffle -----------: " + context.getSpeaker().getTrackList().isShuffling() + "\n";
		description += "Loop --------------: " + context.getSpeaker().getTrackList().getLoopStatus() + "\n";
		description += "Auto Volume ----: " + context.getSpeaker().getVolumeManager().isReducingVolume() + "\n";
		
		embedBuilder.setDescription(description);
		
		Message message = builder.setEmbed(embedBuilder.build()).build();
		TextSender.send(event, message);
	}
	
	@Override
	public String[] getAliases() {
		return new String[] {"settings"};
	}
}