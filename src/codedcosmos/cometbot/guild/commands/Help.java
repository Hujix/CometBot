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
import codedcosmos.cometbot.guild.context.CometGuildContext;
import codedcosmos.hyperdiscord.chat.TextSender;
import codedcosmos.hyperdiscord.command.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Help implements Command {
	
	public static final int COMMANDS_PER_PAGE = 9;
	
	@Override
	public String getName() {
		return "help";
	}
	
	@Override
	public String getHelp() {
		return "Get's the help info from commands";
	}
	
	@Override
	public String[] getStynax() {
		return new String[] {"", "[0-9]"};
	}
	
	@Override
	public void run(MessageReceivedEvent event) throws Exception {
		int argsLength = event.getMessage().getContentDisplay().split(" ").length-1;
		if (argsLength > 5) {
			TextSender.send(event, "Seriously?? " + argsLength + " arguments? Am I a joke to you...?\n" +
					"What where you expecting to happen?");
			
			return;
		}
		
		CometGuildContext context = CometBot.guilds.getContextBy(event);
		context.getDynamicMessages().sendHelpMessage(event.getMessage().getContentDisplay());
	}
	
	@Override
	public String[] getAliases() {
		return new String[] {"info", "huh", "what", "Advice", "Support", "Aid", "Guidance"};
	}
}
