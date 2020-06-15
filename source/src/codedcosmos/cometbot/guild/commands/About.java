package codedcosmos.cometbot.guild.commands;

import codedcosmos.cometbot.core.CometBot;
import codedcosmos.hyperdiscord.chat.TextSender;
import codedcosmos.hyperdiscord.command.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class About implements Command {
	@Override
	public String getName() {
		return "about";
	}
	
	@Override
	public String getHelp() {
		return "Shows a basic about page for cometbot";
	}
	
	@Override
	public String[] getStynax() {
		return new String[] {""};
	}
	
	@Override
	public void run(MessageReceivedEvent event) throws Exception {
		String message = ":\n";
		
		message += "Cometbot v"+ CometBot.VERSION + "\n";
		message += "\n";
		message += "Cometbot is a simple music bot made by codedcosmos, it features high quality audio output, search and a variety of other features.\n";
		message += "\n";
		message += "Github:\n";
		message += "https://github.com/codedcosmos/CometBot \n";
		
		TextSender.send(event, message);
	}
	
	@Override
	public String[] getAliases() {
		return new String[] {"whatisthis", "whoareyou", "cometbot"};
	}
}
