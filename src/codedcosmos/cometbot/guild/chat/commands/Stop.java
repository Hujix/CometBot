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

package codedcosmos.cometbot.guild.chat.commands;

import codedcosmos.cometbot.core.CometBot;
import codedcosmos.cometbot.guild.context.CometGuildContext;
import codedcosmos.hyperdiscord.chat.TextSender;
import codedcosmos.hyperdiscord.command.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Stop implements Command {
	@Override
	public String getName() {
		return "stop";
	}
	
	@Override
	public String getHelp() {
		return "Stops the current song, removing it from the queue.";
	}

	@Override
	public String[] getStynax() {
		return new String[] {""};
	}

	@Override
	public void run(MessageReceivedEvent event) throws Exception {
		CometGuildContext context = CometBot.guilds.getContextBy(event.getGuild());
		context.getSpeaker().stop();
	}

	public String[] getAliases() {
		return new String[] {"Halt", "Stopit"};
	}
}
