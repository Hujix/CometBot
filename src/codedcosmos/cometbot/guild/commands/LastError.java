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

import codedcosmos.hyperdiscord.chat.TextSender;
import codedcosmos.hyperdiscord.command.Command;
import codedcosmos.hyperdiscord.utils.debug.Log;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class LastError implements Command {
	@Override
	public String getName() {
		return "lasterror";
	}
	
	@Override
	public String getHelp() {
		return "Get's the last cached error messages";
	}
	
	@Override
	public String[] getStynax() {
		return new String[] {""};
	}
	
	@Override
	public void run(MessageReceivedEvent event) throws Exception {
		String[] messages = Log.getCache();
		for (String message : messages) {
			String error = "ERROR: " + message + "\n";
			
			TextSender.send(event, error);
		}
	}
	
	@Override
	public String[] getAliases() {
		return new String[] {"Help", "error", "whattheheckwasthatbot"};
	}
}
