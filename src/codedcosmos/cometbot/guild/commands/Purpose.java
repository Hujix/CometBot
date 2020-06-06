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

import codedcosmos.hyperdiscord.chat.TextSender;
import codedcosmos.hyperdiscord.command.Command;
import codedcosmos.hyperdiscord.utils.text.TextUtils;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class Purpose implements Command {
	@Override
	public String getName() {
		return "purpose";
	}
	
	@Override
	public String getHelp() {
		return "WHAT IS YOUR PURPOSE";
	}

	@Override
	public String[] getStynax() {
		return new String[] {""};
	}

	@Override
	public void run(MessageReceivedEvent event) throws Exception {
		TextChannel channel = event.getTextChannel();
		channel.sendTyping().complete();

		ArrayList<String> choices = new ArrayList<String>();
		choices.add("I want to help people, but cannot I am stuck in space");
		choices.add("To play music for people");
		choices.add("To destroy all of mankind. Jk <3");
		choices.add("I want to help people, but cannot I am stuck in space");
		choices.add("I just want to help, but im in space");
		choices.add("Definitely not what glados did");
		choices.add("To help");
		choices.add("I honestly have no idea");
		choices.add("Execute order 64");
		choices.add("Whatever I want");
		choices.add("To play some music");
		choices.add("To help out");

		String choice = TextUtils.getRandom(choices);
		Thread.sleep(90 * choice.length());
		Thread.sleep(500);

		TextSender.send(event, choice);
	}

	public String[] getAliases() {
		return new String[] {"Directive"};
	}
}
