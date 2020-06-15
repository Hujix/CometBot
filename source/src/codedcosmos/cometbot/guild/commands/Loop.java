package codedcosmos.cometbot.guild.commands;

import codedcosmos.cometbot.core.CometBot;
import codedcosmos.cometbot.guild.context.CometGuildContext;
import codedcosmos.hyperdiscord.command.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Loop implements Command {
	@Override
	public String getName() {
		return "loop";
	}
	
	@Override
	public String getHelp() {
		return "Cycles the Loop setting which defines if the queue or song is looping.";
	}
	
	@Override
	public String[] getStynax() {
		return new String[] {""};
	}
	
	@Override
	public void run(MessageReceivedEvent event) throws Exception {
		CometGuildContext context = CometBot.guilds.getContextBy(event.getGuild());
		context.getSpeaker().getTrackList().cycleLooping(event.getTextChannel());
		context.getSpeaker().updateNowPlayingIfNeeded();
	}
	
	public String[] getAliases() {
		return new String[] {"Repeat", "Loopit"};
	}
}
