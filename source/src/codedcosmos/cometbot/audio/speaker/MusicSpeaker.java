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

package codedcosmos.cometbot.audio.speaker;

import codedcosmos.cometbot.audio.speaker.components.AudioSendManager;
import codedcosmos.cometbot.audio.speaker.components.TrackList;
import codedcosmos.cometbot.audio.speaker.components.VolumeManager;
import codedcosmos.cometbot.audio.track.LoadedTrack;
import codedcosmos.cometbot.guild.context.CometGuildContext;
import codedcosmos.hyperdiscord.chat.TextSender;
import codedcosmos.hyperdiscord.utils.debug.Log;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

import java.util.Arrays;

public class MusicSpeaker extends AudioEventAdapter {
	// Status
	private SpeakerStatus status;

	// The currently playing song
	private LoadedTrack currentTrack;
	
	// Components
	private TrackList trackList;
	private AudioSendManager player;
	private VolumeManager volumeManager;
	
	// Guild Context
	private CometGuildContext context;

	public MusicSpeaker(CometGuildContext context) {
		player = new AudioSendManager(this);
		trackList = new TrackList();
		volumeManager = new VolumeManager();
		
		this.context = context;
		
		status = SpeakerStatus.Waiting;
	}

	public void play(boolean announce) {
		try {
			
			if (status == SpeakerStatus.Playing) {
				return;
			}
			
			if (player.isPaused()) {
				if (announce) TextSender.sendThenWait(context.getBotTextChannel(), "Resuming Playback");
				player.unpause();
				status = SpeakerStatus.Playing;
				
				updateNowPlayingIfNeeded();
				
				return;
			}
			
			if (!trackList.hasSongs()) {
				if (announce) {
					TextSender.send(context.getBotTextChannel(), "You must add item's to the queue first!");
					return;
				} else {
					TextSender.send(context.getBotTextChannel(), "Queue is empty, no more songs to play");
					return;
				}
			}
			
			LoadedTrack loadedTrack = trackList.getTrackFromQueue(this);
			
			// Set Current Track
			currentTrack = loadedTrack;
			
			AudioTrack track = loadedTrack.getTrack();
			
			player.startTrack(track);
			status = SpeakerStatus.Playing;
			
			// Send Message
			Log.print("Playing " + track);
			sendNowPlaying();
			
		} catch (Exception e) {
			stop(false);
			Log.printErr(e);
			TextSender.send(context.getBotTextChannel(), "Failed to play song\nError: "+e.getMessage());
		}
	}
	
	public void playSongAgain(TextChannel channel) {
		String[] links = new String[] {currentTrack.getLink()};
		
		addPlay(links, channel, currentTrack.getDJ());
	}
	
	public void skip() {
		player.stopTrack();
		play(true);
	}
	
	public void seek(int seconds) {
		if (status != SpeakerStatus.Paused && status != SpeakerStatus.Playing) {
			TextSender.send(context.getBotTextChannel(), "Cannot seek to point in song, not currently playing a track");
			return;
		}
		
		player.seek(seconds);
		updateNowPlayingIfNeeded();
	}
	
	public void forward(int seconds) {
		if (status != SpeakerStatus.Paused && status != SpeakerStatus.Playing) {
			TextSender.send(context.getBotTextChannel(), "Cannot forward in song, not currently playing a track");
			return;
		}
		
		player.forward(seconds);
		updateNowPlayingIfNeeded();
	}
	
	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		try {
			// Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
			if (trackList.getLoopStatus() == LoopStatus.LoopSong && endReason.mayStartNext) {
				// Clone
				this.player.startTrack(track.makeClone());
				
				// Increment stats
				context.getStatsRecorder().addTracksQueued(1);
				context.getStatsRecorder().addTracksPlayed(1);
				
				return;
			}
			
			context.getDynamicMessages().completeNowPlayingMessagesSong();
			
			status = SpeakerStatus.Waiting;
			
			if (endReason.mayStartNext) {
				play(false);
				return;
			}
		} catch (Exception e) {
			Log.printErr(e);
		}
	}
	
	@Override
	public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
		try {
			Log.printErr("Caught track exception");
			Log.printErr(exception);
			context.getDynamicMessages().completeNowPlayingMessagesSongAsError();
		} catch (Exception e) {
			Log.printErr(e);
			stop(true);
		}
	}
	
	public void clearSongs() {
		trackList.clear();
	}
	
	public void pause(boolean announce) {
		if (status == SpeakerStatus.Waiting) {
			TextSender.send(context.getBotTextChannel(), "No track currently playing, unable to pause");
		} else if (player.isPaused()) {
			if (announce) TextSender.send(context.getBotTextChannel(), "Resuming playback");
			
			player.unpause();
			status = SpeakerStatus.Playing;
			
			context.getDynamicMessages().updateNowPlayingState();
		} else {
			if (announce) TextSender.send(context.getBotTextChannel(), "Pausing");
			
			player.pause();
			status = SpeakerStatus.Paused;
			
			context.getDynamicMessages().updateNowPlayingState();
		}
	}
	
	public void stop(boolean announce) {
		if (status == SpeakerStatus.Waiting) {
			if (announce) TextSender.sendThenWait(context.getBotTextChannel(), "Unable to stop track, Not currently playing a song");
			return;
		}
		
		player.stopTrack();
		clearSongs();
		status = SpeakerStatus.Waiting;
		if (announce) TextSender.sendThenWait(context.getBotTextChannel(), "Stopping track");
	}
	
	public void toggleAutoVolume() {
		if (volumeManager.isReducingVolume()) {
			volumeManager.setReducingVolume(false);
			TextSender.send(context.getBotTextChannel(), "No longer reducing volume when users are talking");
		} else {
			volumeManager.setReducingVolume(true);
			TextSender.send(context.getBotTextChannel(), "Now reducing volume when users are talking");
		}
	}
	
	public void sendNowPlaying() {
		if (status == SpeakerStatus.Playing) {
			context.getDynamicMessages().sendNowPlayingMessage();
		} else {
			TextSender.sendThenWait(context.getBotTextChannel(), "Not currently playing a song");
		}
	}

	public boolean connect(Guild guild, VoiceChannel voiceChannel, TextChannel textChannel) {
		try {
			guild.getAudioManager().setSendingHandler(player.getSendHandler());
			guild.getAudioManager().openAudioConnection(voiceChannel);
			guild.getAudioManager().setReceivingHandler(context.getPacketReciever());
			
			TextSender.send(textChannel, "On my way!");
			return true;
		} catch (InsufficientPermissionException e) {
			TextSender.send(textChannel, "I do not have permission to join that channel");
			return false;
		}
	}
	
	public String getNowPlayingMessageText() {
		int current = trackList.songsPlayed();
		int total = trackList.size()+current;
		String messageText = "("+current+"/"+total+")";
		return messageText;
	}

	public void tick10s() {
		// Now playing
		updateNowPlayingIfNeeded();
	}
	
	public void tick100ms() {
		// Volume manager
		if (status == SpeakerStatus.Playing) {
			volumeManager.tick100ms(context.getPacketReciever().isAudioIsBeingPlayed());
			if (volumeManager.shouldUpdateVolume()) {
				player.setVolume(volumeManager.getVolume());
			}
		}
	}
	
	public void updateNowPlayingIfNeeded() {
		if (status != SpeakerStatus.Waiting) {
			if (!context.getSpeaker().getCurrentTrack().isLiveStream()) {
				context.getDynamicMessages().updateNowPlayingState();
			}
		}
	}
	
	public void addPlay(String link, TextChannel channel, String dj) {
		addPlay(new String[] {link}, channel, dj);
	}
	
	public void addPlay(String[] links, TextChannel channel, String dj) {
		// Add First one (This will block for that one link)
		trackList.addToQueue(this, channel, dj, links, true);
		
		// Check if no links where given, when song is already playing
		if (status == SpeakerStatus.Playing && links.length == 0) {
			TextSender.sendThenWait(context.getBotTextChannel(),"Already Playing a track");
			return;
		}
		
		// Play
		play(true);
		
		// Let user know number of songs in queue, if added
		if (trackList.size() > 1 && links.length != 0) {
			TextSender.sendThenWait(channel,"There are now " + trackList.size() + " songs in the queue!");
		} else if (trackList.size() == 1 && links.length != 0) {
			TextSender.sendThenWait(channel,"There are now 1 song in the queue!");
		}
	}
	
	public void addPlayNext(MessageReceivedEvent event) {
		// Get Links
		String[] args = event.getMessage().getContentRaw().split(" ");
		String[] links = Arrays.copyOfRange(args, 1, args.length);
		
		if (links.length > 1) {
			TextSender.send(event, "There can only be 1 song in the play next queue");
			return;
		}
		if (links.length == 0) {
			TextSender.send(event, "You must specify a song to play next");
			return;
		}
		
		trackList.addPlayNext(this, event.getTextChannel(), links[0], event.getAuthor().getName());
		
		if (status == SpeakerStatus.Playing) {
			TextSender.sendThenWait(event, "Added song to play next");
		} else {
			play(true);
		}
	}
	
	public void leave(String message) {
		TextSender.sendThenWait(context.getBotTextChannel(), message);
		clearSongs();
		stop(false);
	}

	public LoadedTrack getCurrentTrack() {
		return currentTrack;
	}

	public String getCurrentTimestamp() {
		return player.getCurrentTimestamp();
	}

	public long getQueueTimeLength() {
		return trackList.getQueueTimeLength();
	}
	
	public AudioSendManager getPlayer() {
		return player;
	}
	
	public VolumeManager getVolumeManager() {
		return volumeManager;
	}
	
	public SpeakerStatus getStatus() {
		return status;
	}
	
	public TrackList getTrackList() {
		return trackList;
	}
	
	public CometGuildContext getContext() {
		return context;
	}
}
