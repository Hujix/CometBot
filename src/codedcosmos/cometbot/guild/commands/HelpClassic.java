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
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class HelpClassic implements Command {

	private static final int COMMANDS_PER_PAGE = 4;
	
	@Override
	public String getName() {
		return "helpclassic";
	}
	
	@Override
	public String getHelp() {
		return "Gets help from programs";
	}

	@Override
	public String[] getStynax() {
		return new String[] {"", "[0-9]"};
	}

	@Override
	public void run(MessageReceivedEvent event) throws Exception {
		int maxPages = getMaxPages();
		int page = extractPage(event.getMessage().getContentDisplay(), maxPages);

		String helpMessage = "";

		helpMessage += "\n";
		helpMessage += "**Help  -  page " + page + "/" + maxPages + "**";
		helpMessage += "\n\n";

		int index = (page-1) * COMMANDS_PER_PAGE;

		for (int i = index; i < index+COMMANDS_PER_PAGE && i < CometBot.commands.size(); i++) {
			Command command = CometBot.commands.get(i);
			String name = command.getClass().getSimpleName().toLowerCase();

			helpMessage += "**"+name+"**:\n";
			helpMessage += command.getHelp()+"\n";

			helpMessage += "**Usage:\n**";
			for (String usage : command.getStynax()) {
				helpMessage += "-" + name + " " + usage + "\n";
			}

			helpMessage += "\n";
		}
		
		TextSender.send(event, helpMessage);
	}

	private int getMaxPages() {
		float commands = CometBot.commands.size();
		float commandsPerPage = COMMANDS_PER_PAGE;

		int total = Math.max((int)(commands/commandsPerPage), 1);

		return total;
	}

	private int extractPage(String message, int max) {
		String[] lines = message.split(" ");
		if (lines.length < 2) return 1;

		try {
			int page = Integer.parseInt(lines[1]);
			if (page <= 0) return 1;
			if (page > max) return max;

			return page;
		} catch (NumberFormatException e) {
			// Do nothing
		}

		return 1;
	}

	public String[] getAliases() {
		return new String[] {"classicalhelp"};
	}
}
