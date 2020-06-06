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
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Iterator;
import java.util.List;

public class Join implements Command {
	@Override
	public String getName() {
		return "join";
	}
	
	@Override
	public String getHelp() {
		return "Asks the bot to join your current voice channel";
	}

	@Override
	public String[] getStynax() {
		return new String[] {""};
	}

	@Override
	public void run(MessageReceivedEvent event) throws Exception{
		runCommand(event, true);
	}

	public static boolean runCommand(MessageReceivedEvent event, boolean fromJoin) throws Exception {
		User author = event.getAuthor();
		Guild guild = event.getGuild();

		Member member = guild.getMember(author);

		List<VoiceChannel> channels = guild.getVoiceChannels();
		for (Iterator<VoiceChannel> iter = channels.iterator(); iter.hasNext(); ) {
			VoiceChannel channel = iter.next();

			boolean userInChannel = false;
			for (Member memberCheck : channel.getMembers()) {
				if (member.getIdLong() == memberCheck.getIdLong()) {
					userInChannel = true;
					break;
				}
			}

			if (userInChannel) {
				// Check size
				if (channel.getMembers().size() == channel.getUserLimit()) {
					TextSender.send(event, BotMessages.joinButNoRoom.get());
					return false;
				}

				// Get Context
				CometGuildContext context = CometBot.guilds.getContextBy(guild);

				// Ensure it's not already connected
				if (context.isConnectedToVoice()) {
					if (fromJoin) {
						TextSender.send(event, "I have already joined");
					}
					return true;
				}

				// Everything is correct, add to channel
				if (!context.getSpeaker().connect(guild, channel, event.getTextChannel())) {
					return false;
				}

				context.setVoiceChannel(channel);

				return true;
			}
		}

		// User isn't in any channels
		TextSender.send(event, "You must join a voice channel to use this command!");
		return false;
	}

	public String[] getAliases() {
		return new String[] {"Accompany", "Voice", "joinus"};
	}
}
