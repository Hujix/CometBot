/*
 * Discord CometBot by codedcosmos
 *
 * CometBot is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License 3 as published by
 * the Free Software Foundation.
 * CometBot is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License 3 for more details.
 * You should have received a copy of the GNU General Public License 3
 * along with CometBot.  If not, see <https://www.gnu.org/licenses/>.
 */

package codedcosmos.cometbot.guild.commands;

import codedcosmos.cometbot.core.CometBot;
import codedcosmos.hyperdiscord.chat.TextSender;
import codedcosmos.hyperdiscord.command.Command;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class Last implements Command {
	
	@Override
	public String getName() {
		return "last";
	}
	
	@Override
	public String getHelp() {
		return "Repeats the last command";
	}
	
	@Override
	public String[] getStynax() {
		return new String[] {""};
	}
	
	@Override
	public void run(MessageReceivedEvent event) throws Exception {
		List<Message> messages = event.getTextChannel().getHistory().retrievePast(30).complete();
		
		for (Message message : messages) {
			// Check if its a command
			if (!CometBot.commands.isCommand(message)) continue;
			
			// Make sure it's not a last command (recursion)
			if (message.getContentDisplay().startsWith(".last")) continue;
			
			// Trigger new event
			TextSender.send(event,"Executing command: \n\"" + message.getContentDisplay() + "\"");
			MessageReceivedEvent triggerEvent = new MessageReceivedEvent(event.getJDA(), event.getResponseNumber(), message);
			CometBot.commands.processMessageRecievedEvent(triggerEvent);
			
			// Return
			return;
		}
		
		// No Commands found
		TextSender.send(event,"No bot commands found in recent history");
	}
	
	public String[] getAliases() {
		return new String[] {"previous", "lastcommand", "before", "repeatlast"};
	}
}
