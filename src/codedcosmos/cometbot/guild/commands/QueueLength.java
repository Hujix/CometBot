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
import codedcosmos.cometbot.audio.speaker.MusicSpeaker;
import codedcosmos.hyperdiscord.chat.TextSender;
import codedcosmos.hyperdiscord.command.Command;
import codedcosmos.hyperdiscord.utils.text.TextUtils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class QueueLength implements Command {
	@Override
	public String getName() {
		return "queuelength";
	}
	
	@Override
	public String getHelp() {
		return "Gets the length of the current queue";
	}

	@Override
	public String[] getStynax() {
		return new String[] {""};
	}

	@Override
	public void run(MessageReceivedEvent event) throws Exception {
		CometGuildContext context = CometBot.guilds.getContextBy(event);

		String duration = getDuration(context.getSpeaker());
		
		TextSender.send(event, "The queue has " + duration + " worth of songs.");
	}

	public String[] getAliases() {
		return new String[] {"AreWeThereYet", "HowLong"};
	}
	
	public static String getDuration(MusicSpeaker speaker) {
		String duration = TextUtils.convertToMMSS(speaker.getQueueTimeLength());
		return duration;
	}
}
