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

package codedcosmos.cometbot.event;

import codedcosmos.cometbot.core.CometBot;
import codedcosmos.cometbot.event.guild.GuildReadyEventHandler;
import codedcosmos.cometbot.event.guild.GuildVoiceConnectStatus;
import codedcosmos.cometbot.guild.context.CometGuildContext;
import codedcosmos.hyperdiscord.utils.debug.Log;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ReconnectedEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.guild.voice.*;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.events.GenericEvent;

public class EventHandler implements EventListener {
	@Override
	public void onEvent(GenericEvent event) {
		// JDA Events
		if (event instanceof DisconnectEvent) {
			Log.print("Disconnect Event");
		}

		else if (event instanceof ReadyEvent) {
			Log.print("Ready Event");
		}

		else if (event instanceof ReconnectedEvent) {
			Log.print("Reconnected Event");
		}

		// Chat
		else if (event instanceof MessageDeleteEvent) {
			MessageDeleteEvent messageDeleteEvent = (MessageDeleteEvent) event;
			CometGuildContext context = CometBot.guilds.getContextBy(messageDeleteEvent.getGuild());
			context.getDynamicMessages().onMessageDelete(messageDeleteEvent);
		}
		
		// Guild
		else if (event instanceof GuildReadyEvent) {
			GuildReadyEventHandler.onEvent((GuildReadyEvent)event);
		}

		else if (event instanceof GuildVoiceJoinEvent) {
			GuildVoiceConnectStatus.onVoiceJoinEvent((GuildVoiceJoinEvent)event);
		}

		else if (event instanceof GuildVoiceLeaveEvent) {
			GuildVoiceConnectStatus.onVoiceLeaveEvent((GuildVoiceLeaveEvent)event);
		}

		else if (event instanceof GuildVoiceMoveEvent) {
			GuildVoiceConnectStatus.onVoiceMoveEvent((GuildVoiceMoveEvent)event);
		}

		// Emote
		else if (event instanceof GuildMessageReactionAddEvent) {
			CometBot.guilds.sendReactionAddEvent((GuildMessageReactionAddEvent)event);
		}

		else if (event instanceof GuildMessageReactionRemoveEvent) {
			CometBot.guilds.sendReactionRemoveEvent((GuildMessageReactionRemoveEvent)event);
		}
	}

}
