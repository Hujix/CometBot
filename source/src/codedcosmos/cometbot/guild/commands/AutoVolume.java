package codedcosmos.cometbot.guild.commands;

import codedcosmos.cometbot.core.CometBot;
import codedcosmos.cometbot.guild.context.CometGuildContext;
import codedcosmos.hyperdiscord.command.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class AutoVolume implements Command {
	@Override
	public String getName() {
		return "autovolume";
	}
	
	@Override
	public String getHelp() {
		return "Reduces volume when users are speaking";
	}
	
	@Override
	public String[] getStynax() {
		return new String[] {""};
	}
	
	@Override
	public void run(MessageReceivedEvent event) throws Exception {
		//if (event.getMessageIdLong() != 0 || event != null) {
		//	TextSender.send(event, "This command is currently disabled");
		//	return;
		//}
		
		CometGuildContext context = CometBot.guilds.getContextBy(event.getGuild());
		context.getSpeaker().toggleAutoVolume();
	}
	
	@Override
	public String[] getAliases() {
		return new String[] {"deafen", "mute", "quiet"};
	}
}
