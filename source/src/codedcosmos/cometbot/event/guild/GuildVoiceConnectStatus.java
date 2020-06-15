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

package codedcosmos.cometbot.event.guild;

import codedcosmos.cometbot.guild.commands.Leave;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import codedcosmos.hyperdiscord.utils.debug.*;

public class GuildVoiceConnectStatus  {
	public static void onVoiceJoinEvent(GuildVoiceJoinEvent event) {
		Log.print("On Voice Join");
		// TODO
	}

	public static void onVoiceLeaveEvent(GuildVoiceLeaveEvent event) {
		// Check to see how many people are in guild voice channel
		if (event.getChannelLeft().getMembers().size() == 1) {
			// Make sure it's a bot
			if (!event.getChannelLeft().getMembers().get(0).getUser().isBot()) return;
			
			Leave.disconnect(event.getGuild());
			Log.print("Left channel, because noone was in there.");
		}
	}

	public static void onVoiceMoveEvent(GuildVoiceMoveEvent event) {
		// Check to see how many people are in guild voice channel
		if (event.getChannelLeft().getMembers().size() == 1) {
			// Make sure it's a bot
			if (!event.getChannelLeft().getMembers().get(0).getUser().isBot()) return;
			
			Leave.disconnect(event.getGuild());
			Log.print("Left channel, because noone was in there.");
		}
	}
}
