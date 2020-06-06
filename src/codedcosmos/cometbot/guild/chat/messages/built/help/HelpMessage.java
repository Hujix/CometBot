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

package codedcosmos.cometbot.guild.chat.messages.built.help;

import codedcosmos.cometbot.core.CometBot;
import codedcosmos.cometbot.guild.commands.Help;
import codedcosmos.hyperdiscord.chat.messages.BookMessage;
import codedcosmos.hyperdiscord.command.Command;
import codedcosmos.hyperdiscord.command.prebuilt.HelpMessageGenerator;
import codedcosmos.hyperdiscord.guild.GuildContext;
import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;

public class HelpMessage extends BookMessage {
	
	private HelpMessageGenerator helpMessageGenerator;
	private String content;
	
	public HelpMessage(GuildContext context, String content) {
		super(context);
		
		helpMessageGenerator = new HelpMessageGenerator() {
			@Override
			public ArrayList<Command> getCommands() {
				return CometBot.commands.getCommands();
			}
		};
		
		int max = helpMessageGenerator.getMaxPages(Help.COMMANDS_PER_PAGE);
		currentPage = helpMessageGenerator.extractPage(content, max)-1;
	}
	
	@Override
	public Message getNew(int pageNum) {
		return helpMessageGenerator.buildMessage(currentPage, Help.COMMANDS_PER_PAGE);
	}
	
	@Override
	public int getNumPages() {
		return helpMessageGenerator.getMaxPages(Help.COMMANDS_PER_PAGE);
	}
}
