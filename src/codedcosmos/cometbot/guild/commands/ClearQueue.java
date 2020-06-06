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
import codedcosmos.cometbot.fun.BotMessages;
import codedcosmos.cometbot.guild.context.CometGuildContext;
import codedcosmos.hyperdiscord.chat.TextSender;
import codedcosmos.hyperdiscord.command.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ClearQueue implements Command {
	
	@Override
	public String getName() {
		return "clearqueue";
	}
	
	@Override
	public String getHelp() {
		return "Clears the current queue of songs";
	}

	@Override
	public String[] getStynax() {
		return new String[] {""};
	}

	@Override
	public void run(MessageReceivedEvent event) throws Exception {
		CometGuildContext context = CometBot.guilds.getContextBy(event.getGuild());
		
		if (context.getSpeaker().getTrackList().size() > 0) {
			context.getSpeaker().clearSongs();
			TextSender.send(event, BotMessages.clearQueue.get());
		} else {
			TextSender.send(event, "Queue already empty");
		}
	}

	public String[] getAliases() {
		return new String[] {"EmptyQueue"};
	}
}
