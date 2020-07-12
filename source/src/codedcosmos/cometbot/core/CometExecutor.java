package codedcosmos.cometbot.core;

import codedcosmos.cometbot.guild.context.CometGuildContext;
import codedcosmos.hyperdiscord.utils.debug.Log;

public class CometExecutor extends ExecutorThread {
	
	private long lastMillis;
	
	public CometExecutor() {
		super("TickingThread", 10);
		
		lastMillis = System.currentTimeMillis();
	}
	
	@Override
	public void onStart() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			Log.printErr(e);
		}
	}
	
	@Override
	public void onStop() {
	
	}
	
	@Override
	public void run() {
		try {
			// Tick every 10 seconds
			if (System.currentTimeMillis() > lastMillis+(1000*10)) {
				lastMillis = System.currentTimeMillis();
				for (CometGuildContext guild : CometBot.guilds.getGuilds()) {
					guild.getSpeaker().tick10s();
				}
			}
			
			// Tick every 100 milliseconds
			for (CometGuildContext guild : CometBot.guilds.getGuilds()) {
				guild.getSpeaker().tick100ms();
				guild.tick100ms();
			}
			
			// Check for save
			// Save every 24 hours
			if (System.currentTimeMillis() > CometBot.timeSinceLastSave+(1000*60+60*24)) {
				for (CometGuildContext guild : CometBot.guilds.getGuilds()) {
					Log.print("Saving after a 24 hour break");
					CometBot.timeSinceLastSave = System.currentTimeMillis();
					guild.save();
				}
			}
		} catch (Exception e) {
			Log.printErr(e);
		}
	}
}
