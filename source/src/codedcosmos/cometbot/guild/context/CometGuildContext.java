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

package codedcosmos.cometbot.guild.context;

import codedcosmos.cometbot.audio.input.PacketReciever;
import codedcosmos.cometbot.audio.speaker.LoopStatus;
import codedcosmos.cometbot.audio.speaker.MusicSpeaker;
import codedcosmos.cometbot.audio.speaker.SpeakerStatus;
import codedcosmos.cometbot.database.CometSave;
import codedcosmos.cometbot.guild.context.sub.StatsRecorder;
import codedcosmos.cometbot.utils.web.LinkConverter;
import codedcosmos.hyperdiscord.guild.GuildContext;
import codedcosmos.hyperdiscord.utils.debug.Log;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class CometGuildContext extends GuildContext {
	
	// Voice
	private MusicSpeaker speaker;
	private boolean isConnectedToVoice;
	private VoiceChannel voiceChannel;
	
	private PacketReciever packetReciever;
	
	// Chat
	private DynamicMessages dynamicMessages;
	
	// Saves
	private boolean fileIODisabled = false;
	private CometSave configSave;
	private CometSave likesSave;
	private StatsRecorder statsRecorder;
	
	public CometGuildContext(Guild guild) {
		super(guild);
		Log.print("Loading " + guild.getName());
		
		// Setup basic state
		this.speaker = new MusicSpeaker(this);
		packetReciever = new PacketReciever();
		isConnectedToVoice = false;
		
		// Setup dynamic messages
		dynamicMessages = new DynamicMessages(this);
		
		// Load saves
		Log.print("Loading saves for " + guild.getId());
		try {
			// Load config
			configSave = new CometSave(new File("saves/servers/"+guild.getId()+"/config.cometsave"));
			
			HashMap<String, String> configExpects = new HashMap<String, String>();
			configExpects.put("shuffle", "false");
			configExpects.put("loop", LoopStatus.NoLoop.toString());
			
			configSave.expect(configExpects);
			
			// Load likes
			likesSave = new CometSave(new File("saves/servers/"+guild.getId()+"/likes.cometsave"));
			
			// Stats
			statsRecorder = new StatsRecorder(guild);
			statsRecorder.load();
			
			// Apply Loaded
			applyConfig();
		} catch (IOException e) {
			Log.printErr(e);
			fileIODisabled = true;
		}
		Log.print("Fully loaded " + guild.getId()+":"+guild.getName());
	}
	
	// Tick
	public void tick100ms() {
		if (speaker.getStatus() == SpeakerStatus.Playing) {
			packetReciever.tick100ms();
		}
	}
	
	// Shutdown
	public void shutdown() {
		speaker.stop(true);
		
		dynamicMessages.forceCompleteSearchMessage();
		dynamicMessages.forceCompleteHelpMessage();
		dynamicMessages.forceCompleteQueueMessage();
		
		save();
		
		Log.print("Shutdown guild " + guild.getName());
	}
	
	// Saves
	public void applyConfig() {
		if (fileIODisabled) {
			Log.printErr("FileIO is currently disabled for guild '" + guild.getName() + "'");
			Log.printErr("Cannot perform apply config");
			return;
		}
		
		// Load shuffle
		try {
			boolean shuffle = Boolean.parseBoolean(configSave.get("shuffle"));
			speaker.getTrackList().setShuffling(shuffle);
		} catch (NumberFormatException e) {
			Log.printErr(e);
			configSave.set("shuffle", "false");
		}
		
		// Load loop
		try {
			LoopStatus status = LoopStatus.valueOf(configSave.get("loop"));
			speaker.getTrackList().setLoopStatus(status);
		} catch (IllegalArgumentException e) {
			Log.printErr(e);
			configSave.set("loop", LoopStatus.NoLoop.toString());
		}
	}
	
	public void save() {
		if (fileIODisabled) {
			Log.printErr("FileIO is currently disabled for guild '" + guild.getName() + "'");
			Log.printErr("Cannot perform save operation");
			return;
		}
		
		// Update config values
		configSave.set("loop", speaker.getTrackList().getLoopStatus().toString());
		configSave.set("shuffle", speaker.getTrackList().isShuffling()+"");
		
		// Save
		try {
			configSave.save();
			likesSave.save();
			statsRecorder.save();
		} catch (IOException e) {
			Log.printErr(e);
			fileIODisabled = true;
		}
		
		Log.print("Saved save files!");
	}
	
	public void incrementSongLikes(String link, int increment) {
		link = LinkConverter.makeLinkRigid(link);
		try {
			String stringValue = likesSave.getOr(link, "0");
			int value = Integer.parseInt(stringValue);
			likesSave.set(link, (value+increment)+"");
		} catch (NumberFormatException e) {
			Log.printErr(e);
		}
	}
	
	// Reactions
	@Override
	public void onReactionAdd(GuildMessageReactionAddEvent event) {
		dynamicMessages.onReactionAdd(event);
	}
	
	@Override
	public void onReactionRemove(GuildMessageReactionRemoveEvent event) {
		dynamicMessages.onReactionRemove(event);
	}
	
	// Getters
	public MusicSpeaker getSpeaker() {
		return speaker;
	}
	
	public void disconnectFromVoice(String message) {
		isConnectedToVoice = false;
		guild.getAudioManager().closeAudioConnection();
		speaker.leave(message);
	}
	
	public void connectToVoice(VoiceChannel voicechannel) {
		this.voiceChannel = voicechannel;
		isConnectedToVoice = true;
	}
	
	public boolean isConnectedToVoice() {
		return isConnectedToVoice;
	}
	
	public VoiceChannel getVoiceChannel() {
		return voiceChannel;
	}
	
	public DynamicMessages getDynamicMessages() {
		return dynamicMessages;
	}
	
	public PacketReciever getPacketReciever() {
		return packetReciever;
	}
	
	public StatsRecorder getStatsRecorder() {
		return statsRecorder;
	}
	
	public CometSave getLikesSave() {
		return likesSave;
	}
}
