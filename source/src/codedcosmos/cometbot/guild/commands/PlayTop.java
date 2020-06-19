package codedcosmos.cometbot.guild.commands;

import codedcosmos.cometbot.core.CometBot;
import codedcosmos.cometbot.guild.context.CometGuildContext;
import codedcosmos.cometbot.utils.songs.OrderedLink;
import codedcosmos.hyperdiscord.chat.TextSender;
import codedcosmos.hyperdiscord.command.Command;
import codedcosmos.hyperdiscord.utils.debug.Log;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.*;

public class PlayTop implements Command {
	@Override
	public String getName() {
		return "play";
	}
	
	@Override
	public String getHelp() {
		return "Plays music. List of supported websites:\n" +
				"Youtube, SoundCloud, Bandcamp, Vimeo, Twitch Streams";
	}
	
	@Override
	public String[] getStynax() {
		return new String[] {"", "[links...]"};
	}
	
	@Override
	public void run(MessageReceivedEvent event) throws Exception {
		// Join the channel
		if (!Join.runCommand(event, false)) return;
		
		// Get Context
		CometGuildContext context = CometBot.guilds.getContextBy(event);
		
		// Args
		String[] args = event.getMessage().getContentRaw().split(" ");
		args = Arrays.copyOfRange(args, 1, args.length);
		
		if (args.length > 1) {
			TextSender.send(event, "To many arguments, there should only be 1 argument that is a number!");
			return;
		}
		
		// Get int
		int n = 1;
		try {
			n = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			Log.printErr(e);
			TextSender.send(event, "Argument must be an whole number");
			return;
		}
		
		// Convert raw song/like data into list of OrderedLinks
		HashMap<String, String> values = context.getLikesSave().getValues();
		ArrayList<OrderedLink> links = new ArrayList<OrderedLink>();
		
		for (Map.Entry<String, String> entry : values.entrySet()) {
			String link = entry.getKey();
			String value = entry.getValue();
			
			int numLikes = 0;
			try {
				numLikes = Integer.parseInt(value);
			} catch (NumberFormatException e) {
				Log.printErr(e);
				continue;
			}
			
			if (link.startsWith("www.youtube.com-watch?v")) {
				String youtubeid = link.substring("www.youtube.com-watch?v".length());
				String truelink = "https://www.youtube.com/watch?v="+youtubeid;
				
				links.add(new OrderedLink(truelink, numLikes));
			} else {
				// Invalid link
			}
		}
		
		// Sort
		Collections.sort(links, new Comparator<OrderedLink>() {
			@Override
			public int compare(OrderedLink o1, OrderedLink o2) {
				return Integer.valueOf(o2.getLikes()).compareTo(Integer.valueOf(o1.getLikes()));
			}
		});
		
		// Get first n values
		ArrayList<OrderedLink> topNLinks = new ArrayList<OrderedLink>();
		for (int i = 0; i < n; i++) {
			if (links.size() == 0) break;
			
			topNLinks.add(links.get(0));
			links.remove(0);
		}
		
		// Get raw links
		String[] strLinks = new String[topNLinks.size()];
		
		// Play links
		for (int i = 0; i < strLinks.length; i++) {
			strLinks[i] = topNLinks.get(i).getLink();
		}
		
		if (strLinks.length == 0) {
			TextSender.send(event, "Could not find any liked tracks! Like tracks by clicking the heart while a track is playing!");
			return;
		} else if (strLinks.length < n) {
			TextSender.send(event, "Only found " + strLinks.length + " out of the " + n + " requested tracks!");
		}
		
		// Play
		context.getSpeaker().addPlay(strLinks, event.getTextChannel(), event.getAuthor().getName());
	}
	
	public String[] getAliases() {
		return new String[] {"top10", "topn", "favorites"};
	}
}