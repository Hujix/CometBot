package codedcosmos.cometbot.guild.commands;

import codedcosmos.cometbot.core.CometBot;
import codedcosmos.cometbot.guild.context.CometGuildContext;
import codedcosmos.hyperdiscord.chat.TextSender;
import codedcosmos.hyperdiscord.command.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Rewind implements Command {
	@Override
	public String getName() {
		return "rewind";
	}
	
	@Override
	public String getHelp() {
		return "Subtracts a couple seconds to where the track is playing";
	}
	
	@Override
	public String[] getStynax() {
		return new String[] {"(Time in Seconds)"};
	}
	
	@Override
	public void run(MessageReceivedEvent event) throws Exception {
		CometGuildContext context = CometBot.guilds.getContextBy(event.getGuild());
		
		String[] text = event.getMessage().getContentRaw().split(" ");
		if (text.length != 2) {
			TextSender.send(event, "You need to specify time in seconds");
		}
		
		try {
			int time = Integer.parseInt(text[1]);
			context.getSpeaker().forward(-time);
		} catch (NumberFormatException e) {
			TextSender.send(event, "Invalid time signature");
		}
	}
	
	public String[] getAliases() {
		return new String[] {"subtrack", "goback"};
	}
}