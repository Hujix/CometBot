package codedcosmos.cometbot.guild.commands;

import codedcosmos.cometbot.core.CometBot;
import codedcosmos.cometbot.guild.context.CometGuildContext;
import codedcosmos.hyperdiscord.chat.TextSender;
import codedcosmos.hyperdiscord.command.Command;
import codedcosmos.hyperdiscord.utils.debug.Log;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Save implements Command {
	
	private static long lastSaveTime = System.currentTimeMillis()-getHour();
	
	@Override
	public String getName() {
		return "save";
	}
	
	@Override
	public String getHelp() {
		return "Saves state and config to disk";
	}
	
	@Override
	public String[] getStynax() {
		return new String[] {""};
	}
	
	@Override
	public void run(MessageReceivedEvent event) throws Exception {
		long now = System.currentTimeMillis();
		long expected = lastSaveTime+getHour();
		if (now > expected) {
			CometGuildContext context = CometBot.guilds.getContextBy(event.getGuild());
			context.save();
			
			TextSender.send(event, "Successfully saved state");
			
			lastSaveTime = System.currentTimeMillis();
		} else {
			Log.print("expected " + expected);
			Log.print("now " + now);
			TextSender.send(event, "Cannot save state, saving can only occur every 1 hour.");
			TextSender.send(event, "You will need to wait " + ((expected-now)/1000/60) + " minutes before saving!");
		}
	}
	
	public String[] getAliases() {
		return new String[] {"backup"};
	}
	
	private static int getHour() {
		return (1000*60*60);
	}
}
