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

package codedcosmos.cometbot.guild.chat.messages;

import codedcosmos.cometbot.core.CometBot;
import codedcosmos.cometbot.guild.context.CometGuildContext;
import codedcosmos.hyperdiscord.chat.TextSender;
import codedcosmos.hyperdiscord.command.CommandListener;
import codedcosmos.hyperdiscord.utils.debug.Log;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;

public class CometCommandListener extends CommandListener<CometGuildContext> {
	public CometCommandListener() {
		super("codedcosmos.cometbot.guild.commands", ".");
	}
	
	@Override
	public CometGuildContext addGuild(Guild guild) {
		return CometBot.guilds.addGuild(guild);
	}
	
	@Override
	public void onCommandExecutionException(Exception e, MessageReceivedEvent event) {
		if (e instanceof PermissionException) {
			TextSender.send(event, "I don't have permission to do that!");
			TextSender.send(event, "Permission Required " + ((PermissionException) e).getPermission());
			return;
		}
		
		TextSender.send(event, "Error:\nRecieved a `" + e.getClass().getCanonicalName() + "` when trying to execute command!");
		
		Log.printErr("Caught Exception when trying to execute command!");
		Log.printErr("Command Message: '" + event.getMessage().getContentRaw() + "'");
		Log.printErr("");
		Log.printErr(e);
	}
}
