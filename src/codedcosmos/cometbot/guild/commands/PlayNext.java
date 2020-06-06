package codedcosmos.cometbot.guild.commands;

import codedcosmos.cometbot.core.CometBot;
import codedcosmos.cometbot.guild.context.CometGuildContext;
import codedcosmos.hyperdiscord.command.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PlayNext implements Command {
	@Override
	public String getName() {
		return "playnext";
	}
	
	@Override
	public String getHelp() {
		return "Plays the chosen song immediately after the current one";
	}
	
	@Override
	public String[] getStynax() {
		return new String[] {"(link)"};
	}
	
	@Override
	public void run(MessageReceivedEvent event) throws Exception {
		// Join the channel
		if (!Join.runCommand(event, false)) return;
		
		// Get Context
		CometGuildContext context = CometBot.guilds.getContextBy(event);
		context.getSpeaker().addPlayNext(event);
	}
	
	public String[] getAliases() {
		return new String[] {"playafter"};
	}
}