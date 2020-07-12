package codedcosmos.cometbot.guild.commands;

import codedcosmos.hyperdiscord.chat.TextSender;
import codedcosmos.hyperdiscord.command.Command;
import codedcosmos.hyperdiscord.utils.debug.Log;
import codedcosmos.hyperdiscord.utils.text.TextSplitter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Changelog implements Command {
	@Override
	public String getName() {
		return "changelog";
	}
	
	@Override
	public String getHelp() {
		return "Shows the changes for the current version";
	}
	
	@Override
	public String[] getStynax() {
		return new String[] {""};
	}
	
	@Override
	public void run(MessageReceivedEvent event) throws Exception {
		try {
			InputStream inputStream = ClassLoader.getSystemClassLoader().getSystemResourceAsStream("changelog.md");
			InputStreamReader streamReader = new InputStreamReader(inputStream, "UTF-8");
			BufferedReader in = new BufferedReader(streamReader);
			
			String text = ":";
			
			for (String line; (line = in.readLine()) != null;) {
				text += "\n" + line;
			}
			
			// Split if to big
			String[] messages = new String[] {text};
			if (text.length() >= 2000-1) {
				messages = TextSplitter.split(text, 1999);
			}
			
			// Send
			for (String message : messages) {
				TextSender.sendThenWait(event, message);
			}
			
		} catch (IOException e) {
			TextSender.send(event, "Failed to load changelog file!");
			Log.printErr(e);
		}
	}
	
	@Override
	public String[] getAliases() {
		return new String[] {"update"};
	}
}
