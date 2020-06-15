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
import codedcosmos.cometbot.guild.chat.messages.built.search.SearchMessage;
import codedcosmos.cometbot.guild.context.CometGuildContext;
import codedcosmos.hyperdiscord.chat.TextSender;
import codedcosmos.hyperdiscord.command.Command;
import codedcosmos.hyperdiscord.guild.GuildContext;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Search implements Command {
	@Override
	public String getName() {
		return "search";
	}
	
	@Override
	public String getHelp() {
		return "Searches for tracks on the internet";
	}
	
	@Override
	public String[] getStynax() {
		return new String[] {"<search query>"};
	}
	
	@Override
	public void run(MessageReceivedEvent event) throws Exception {
		// Join the channel
		if (!Join.runCommand(event, false)) return;
		
		//                                                  ".search " = 8
		String search = event.getMessage().getContentDisplay().substring(8);
		
		if (search.length() <= 0) {
			TextSender.send(event, "You must specify a search query");
			return;
		}
		
		CometGuildContext context = CometBot.guilds.getContextBy(event);
		context.getDynamicMessages().sendSearchMessage(search);
	}
	
	public String[] getAliases() {
		return new String[] {"internet", "findsong", "lookup", "google", "interweb"};
	}
}
