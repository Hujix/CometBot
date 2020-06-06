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

package codedcosmos.cometbot.fun;

import java.util.ArrayList;

public class BotMessages {
	public static BotMessageBank clearQueue = new BotMessageBank("Cleared the queue for you", "Cleared the queue", "Goodbye queue of songs");
	public static BotMessageBank joinButNoRoom = new BotMessageBank("Sorry, there doesn't seem to be any room there.", "Not enough room in that channel", "Hey, can you go to another channel with more room");
}
