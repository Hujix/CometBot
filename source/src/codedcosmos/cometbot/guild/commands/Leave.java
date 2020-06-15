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
import codedcosmos.hyperdiscord.chat.TextSender;
import codedcosmos.hyperdiscord.command.Command;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Leave implements Command {
	private int leaveCount = 0;
	
	@Override
	public String getName() {
		return "leave";
	}
	
	@Override
	public String getHelp() {
		return "Leaves the current channel";
	}

	@Override
	public String[] getStynax() {
		return new String[] {""};
	}

	@Override
	public void run(MessageReceivedEvent event) throws Exception {
		CometGuildContext context = CometBot.guilds.getContextBy(event.getGuild());

		if (leaveCheck(event, context)) return;
		
		disconnect(event.getGuild());
	}

	public static void disconnect(Guild guild) {
		guild.getAudioManager().closeAudioConnection();
		
		CometGuildContext context = CometBot.guilds.getContextBy(guild);
		context.disconnectFromVoice();
	}

	public String[] getAliases() {
		return new String[] {"Go", "Quit", "Exit", "Leave"};
	}
	
	private boolean leaveCheck(MessageReceivedEvent event, CometGuildContext context) {
		if (!context.isConnectedToVoice()) {
			leaveCount++;
			switch (leaveCount) {
				case 1: {
					TextSender.send(event, "Not in a channel yet ^_^");
					break;
				}
				case 2: {
					TextSender.send(event, "I already left, alright");
					break;
				}
				case 3: {
					TextSender.send(event, "I'm gone, you don't have to ask again");
					break;
				}
				case 4: {
					TextSender.send(event, "Oh, your stupid, sorry my mistake");
					break;
				}
				case 5: {
					TextSender.send(event, "Seriously stop");
					break;
				}
				case 6: {
					TextSender.send(event, "IM NOT IN THE VOICE CHANNEL");
					break;
				}
				case 7: {
					TextSender.send(event, "If you hate me so much at least tell me why");
					break;
				}
				case 8: {
					TextSender.send(event, "SERIOUSLY STOP");
					break;
				}
				case 9: {
					TextSender.send(event, "...");
					break;
				}
				case 10: {
					TextSender.send(event, "You think i'm some kind of joke");
					break;
				}
				case 11: {
					TextSender.send(event, "If you hate me so much at least tell me why");
					break;
				}
				case 12: {
					TextSender.send(event, "I'm not going to talk to you");
					break;
				}
				
				case 20: {
					TextSender.send(event, "This is the last secret, you ain't gonna get any more");
					break;
				}
				
				case 30: {
					TextSender.send(event, "Ok, your actually pretty dedicated, let's see if you can hit 100");
					break;
				}
				
				case 50: {
					TextSender.send(event, "Half way there... Nice?");
					break;
				}
				
				case 100: {
					TextSender.send(event, "Congrats, you wasted your time");
					break;
				}
				
				case 1000: {
					TextSender.send(event, "Ok seriously how did you make it this far");
					break;
				}
				
				default: {
					if (leaveCount > 30) {
						TextSender.send(event, ">:(  " + leaveCount);
					} else {
						TextSender.send(event, "...");
					}
					break;
				}
			}
			
			return true;
		} else {
			leaveCount = 0;
			return false;
		}
	}
}
