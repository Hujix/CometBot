package codedcosmos.cometbot.guild.commands;

import codedcosmos.hyperdiscord.chat.TextSender;
import codedcosmos.hyperdiscord.command.Command;
import codedcosmos.hyperdiscord.utils.debug.Log;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.*;
import java.net.URL;

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
			
			TextSender.send(event, text);
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
