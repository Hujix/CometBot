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

package codedcosmos.cometbot.guild.chat.messages.built.queue;

import codedcosmos.cometbot.guild.commands.QueueLength;
import codedcosmos.cometbot.guild.context.CometGuildContext;
import codedcosmos.cometbot.audio.speaker.MusicSpeaker;
import codedcosmos.cometbot.audio.track.LoadedTrack;
import codedcosmos.hyperdiscord.chat.messages.BookMessage;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;

public class QueueMessage extends BookMessage {
	
	private static final int SONGS_PER_PAGE = 10;
	private MusicSpeaker speaker;
	
	public QueueMessage(CometGuildContext context) {
		super(context);
		this.speaker = context.getSpeaker();
	}
	
	@Override
	public Message getNew(int page) {
		int maxPages = getNumPages();
		int numItems = speaker.getTrackList().size();
		
		// Create message builder and embed builder
		MessageBuilder builder = new MessageBuilder();
		
		// Set base content
		String messageContent = "";
		
		messageContent += "\n";
		messageContent += "**Queue**  -  **Page** " + (page+1) + "/" + maxPages + "\n";
		messageContent += "**Queue Length**: " + QueueLength.getDuration(speaker) + " - **Number of songs**: " + speaker.getTrackList().size();
		messageContent += "\n\n";
		
		
		
		// Prepare and iterate
		int index = page * SONGS_PER_PAGE;
		
		for (int i = index; i < index+SONGS_PER_PAGE && i < numItems; i++) {
			LoadedTrack track = speaker.getTrackList().getTrack(i);
			messageContent += i + ") " + track.getSongTitle() + "\n";
		}
		
		builder.setContent(messageContent);
		// Return
		return builder.build();
	}
	
	@Override
	public int getNumPages() {
		double commands = speaker.getTrackList().size();
		double commandsPerPage = SONGS_PER_PAGE;
		double divInt = Math.ceil(commands/commandsPerPage);
		
		int total = Math.max((int)divInt, 1);
		
		return total;
	}
}
