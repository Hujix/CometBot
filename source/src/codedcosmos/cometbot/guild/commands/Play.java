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
import codedcosmos.cometbot.utils.web.SearchTrack;
import codedcosmos.cometbot.utils.web.YoutubeSearcher;
import codedcosmos.hyperdiscord.chat.TextSender;
import codedcosmos.hyperdiscord.command.Command;
import codedcosmos.hyperdiscord.utils.debug.Log;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class Play implements Command {
	@Override
	public String getName() {
		return "play";
	}
	
	@Override
	public String getHelp() {
		return "Plays music. List of supported websites:\n" +
				"Youtube, SoundCloud, Bandcamp, Vimeo, Twitch Streams";
	}

	@Override
	public String[] getStynax() {
		return new String[] {"", "[links...]"};
	}

	@Override
	public void run(MessageReceivedEvent event) throws Exception {
		// Join the channel
		if (!Join.runCommand(event, false)) return;
		
		// Get Context
		CometGuildContext context = CometBot.guilds.getContextBy(event);
		
		// Args
		String[] args = event.getMessage().getContentRaw().split(" ");
		args = Arrays.copyOfRange(args, 1, args.length);
		
		// Check if they are links or a search term
		boolean allAreUrls = true;
		for (int i = 0; i < args.length; i++) {
			if (!args[i].startsWith("www.") && !args[i].startsWith("https://") && !args[i].startsWith("http://")) {
				allAreUrls = false;
				break;
			}
		}
		
		if (allAreUrls) {
			// Add direct link
			context.getSpeaker().addPlay(args, event.getTextChannel(), event.getAuthor().getName());
		} else {
			StringBuilder stringBuilder = new StringBuilder();
			for (int i = 0; i < args.length; i++) {
				stringBuilder.append(args[i]).append(" ");
			}
			ArrayList<SearchTrack> results = YoutubeSearcher.searchForSongs(stringBuilder.toString(), 1);
			if (results.size() == 0) {
				TextSender.send(event, "No Results found");
				return;
			}
			TextSender.send(event, "Found song on youtube");
			
			// Add Searched song
			context.getSpeaker().addPlay(results.get(0).getLink(), event.getTextChannel(), event.getAuthor().getName());
		}
	}

	public String[] getAliases() {
		return new String[] {"Begin", "Start", "resume"};
	}
}
