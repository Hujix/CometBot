package codedcosmos.cometbot.guild.context.sub;

import codedcosmos.cometbot.database.CometSave;
import codedcosmos.hyperdiscord.utils.debug.Log;
import net.dv8tion.jda.api.entities.Guild;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class StatsRecorder {
	private CometSave statsSave;
	
	public StatsRecorder(Guild guild) throws IOException {
		statsSave = new CometSave(new File("saves/servers/"+guild.getId()+"/stats.cometsave"));
	}
	
	public void load() {
		HashMap<String, String> statsExpects = new HashMap<String, String>();
		statsExpects.put("tracksQueued", "0");
		statsExpects.put("tracksPlayed", "0");
		statsExpects.put("likes", "0");
		statsExpects.put("commandsEntered", "0");
		statsExpects.put("goodbots", "0");
		
		statsSave.expect(statsExpects);
		
		try {
			tracksQueued = Integer.parseInt(statsSave.get("tracksQueued"));
			tracksPlayed = Integer.parseInt(statsSave.get("tracksPlayed"));
			likes = Integer.parseInt(statsSave.get("likes"));
			commandsEntered = Integer.parseInt(statsSave.get("commandsEntered"));
			goodbots = Integer.parseInt(statsSave.get("goodbots"));
		} catch (NumberFormatException e) {
			Log.printErr(e);
		}
		Log.print("Loaded stats");
	}
	
	public void save() throws IOException {
		statsSave.set("tracksQueued", tracksQueued+"");
		statsSave.set("tracksPlayed", tracksPlayed+"");
		statsSave.set("likes", likes+"");
		statsSave.set("commandsEntered", commandsEntered+"");
		statsSave.set("goodbots", goodbots+"");
		
		statsSave.save();
	}
	
	// Stats
	private int tracksQueued = 0;
	public void addTracksQueued(int x) {
		tracksQueued += x;
	}
	public int getTracksQueued() {
		return tracksQueued;
	}
	
	private int tracksPlayed = 0;
	public void addTracksPlayed(int x) {
		tracksPlayed += x;
	}
	public int getTracksPlayed() {
		return tracksPlayed;
	}
	
	private int likes = 0;
	public void addLikes(int x) {
		likes += x;
	}
	public int getLikes() {
		return likes;
	}
	
	private int commandsEntered = 0;
	public void addCommandsEntered(int x) {
		commandsEntered += x;
	}
	public int getCommandsEntered() {
		return commandsEntered;
	}
	
	private int goodbots = 0;
	public void addGoodbots(int x) {
		goodbots += x;
	}
	public int getGoodbots() {
		return goodbots;
	}
}
