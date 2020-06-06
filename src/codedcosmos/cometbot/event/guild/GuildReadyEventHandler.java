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

import codedcosmos.cometbot.core.CometBot;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;

public class GuildReadyEventHandler {
	public static void onEvent(GuildReadyEvent event) {
		CometBot.guilds.addGuild(event.getGuild());
	}
}
