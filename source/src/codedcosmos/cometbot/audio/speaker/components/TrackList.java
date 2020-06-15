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

package codedcosmos.cometbot.audio.speaker.components;

import codedcosmos.cometbot.audio.speaker.LoopStatus;
import codedcosmos.cometbot.audio.track.LoadedTrack;
import codedcosmos.cometbot.audio.lava.MusicPlayer;
import codedcosmos.hyperdiscord.chat.TextSender;
import codedcosmos.hyperdiscord.utils.debug.Log;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;

public class TrackList {
	private CopyOnWriteArrayList<LoadedTrack> queue;
	
	// Status
	private boolean isShuffling;
	private LoopStatus loopStatus;
	
	// Random
	private Random random;
	
	// Number of songs played in a session
	private int songsPlayed = 0;
	
	// Last song
	private LoadedTrack lastSong;
	
	// PlayNext
	private boolean playNextAvaliable = false;
	private LoadedTrack playNext;
	
	public TrackList() {
		isShuffling = false;
		loopStatus = LoopStatus.NoLoop;
		random = new Random();
		
		queue = new CopyOnWriteArrayList<LoadedTrack>();
	}
	
	// Adding
	public void addSong(AudioTrack audioTrack, String dj, String link) {
		LoadedTrack track = new LoadedTrack(audioTrack, dj, link);
		queue.add(track);
		Log.print("Added song: " + track.summary());
	}
	
	public void addSongs(AudioPlaylist audioPlaylist, String dj, String link) {
		int songsAdded = 0;
		
		for (AudioTrack audioTrack : audioPlaylist.getTracks()) {
			LoadedTrack track = new LoadedTrack(audioTrack, dj, link);
			queue.add(track);
			songsAdded++;
		}
		
		Log.print("Added " + songsAdded + "/" + audioPlaylist.getTracks().size() + " songs from playlist: " + link);
	}
	
	public void addToQueue(TextChannel channel, String dj, String[] links, boolean block) {
		links = processLinks(links);
		
		ArrayList<Future<Void>> futures = new ArrayList<Future<Void>>(links.length);
		
		for (String link : links) {
			Future<Void> queueThread = MusicPlayer.getPlayerManager().loadItemOrdered(channel, link, new AudioLoadResultHandler() {
				@Override
				public void trackLoaded(AudioTrack audioTrack) {
					addSong(audioTrack, dj, link);
				}
				
				@Override
				public void playlistLoaded(AudioPlaylist audioPlaylist) {
					addSongs(audioPlaylist, dj, link);
				}
				
				@Override
				public void noMatches() {
					TextSender.sendThenWait(channel, "Failed to load song '" + link + "', no matches avaliable.");
				}
				
				@Override
				public void loadFailed(FriendlyException e) {
					printLoadFailed(e, channel);
				}
			});
			
			futures.add(queueThread);
		}
		
		if (!block) return;
		
		while (futures.size() > 0) {
			boolean futuresDone = true;
			
			for (Future<Void> future : futures) {
				if (!(future.isCancelled() || future.isDone())) futuresDone = false;
			}
			
			if (futuresDone) break;
		}
	}
	
	public void addPlayNext(TextChannel channel, String link, String dj) {
		Future<Void> future = MusicPlayer.getPlayerManager().loadItemOrdered(channel, link, new AudioLoadResultHandler() {
			@Override
			public void trackLoaded(AudioTrack audioTrack) {
				addPlayNext(audioTrack, dj, link);
			}
			
			@Override
			public void playlistLoaded(AudioPlaylist audioPlaylist) {
				TextSender.sendThenWait(channel, "Play next song cannot be a playlist");
			}
			
			@Override
			public void noMatches() {
				TextSender.sendThenWait(channel, "Failed to load song, no matches avaliable.");
			}
			
			@Override
			public void loadFailed(FriendlyException e) {
				printLoadFailed(e, channel);
			}
		});
		
		while (true) {
			if (future.isCancelled() || future.isDone()) break;
		}
	}
	
	public void addPlayNext(AudioTrack track, String dj, String link) {
		playNext = new LoadedTrack(track, dj, link);
		playNextAvaliable = true;
	}
	
	// Utils
	private String[] processLinks(String[] links) {
		for (int i = 0; i < links.length; i++) {
			if (links[i].startsWith("www.")) {
				links[i] = "https://"+links[i];
			}
		}
		return links;
	}
	
	// Removing
	public void clear() {
		queue.clear();
		songsPlayed = 0;
	}
	
	// Retrieving
	public LoadedTrack getTrackFromQueue() {
		// Increment total
		songsPlayed++;
		
		// If play next choose that
		if (playNextAvaliable) {
			playNextAvaliable = false;
			return playNext;
		}
		
		if (loopStatus != LoopStatus.LoopSong || lastSong == null) {
			int i = isShuffling ? random.nextInt(queue.size()) : 0;
			LoadedTrack loadedTrack = queue.get(i);
			
			lastSong = loadedTrack;
			
			// Remove song if not looping
			if (loopStatus != LoopStatus.NoLoop) {
				queue.add(loadedTrack.makeClone());
			}
			queue.remove(i);
			
			return loadedTrack;
		} else { // Loop song
			return lastSong.makeClone();
		}
	}
	
	// Queue Size
	public boolean hasSongs() {
		return queue.size() > 0 || playNextAvaliable;
	}
	
	public int size() {
		return queue.size();
	}
	
	// Configuration
	public void toggleShuffle(TextChannel channel) {
		if (isShuffling) {
			setShuffling(channel, false);
		} else {
			setShuffling(channel, true);
		}
	}
	
	public void setShuffling(TextChannel channel, boolean shuffle) {
		if (shuffle) {
			isShuffling = true;
			TextSender.send(channel, "Now Shuffling Tracks");
		} else {
			isShuffling = false;
			TextSender.send(channel, "No Longer Shuffling Tracks");
		}
	}
	
	public void cycleLooping(TextChannel channel) {
		if (loopStatus == LoopStatus.NoLoop) {
			loopStatus = LoopStatus.Loop;
			TextSender.send(channel, "Now looping songs");
		} else if (loopStatus == LoopStatus.Loop) {
			loopStatus = LoopStatus.LoopSong;
			TextSender.send(channel, "Now looping a single song");
		} else { // Loop song
			loopStatus = LoopStatus.NoLoop;
			TextSender.send(channel, "Not looping Tracks");
		}
	}
	
	// Time length
	public long getQueueTimeLength() {
		long length = 0;
		
		for (LoadedTrack track : queue) {
			length += track.getSongLength();
		}
		
		return length;
	}
	
	// Getters
	public int songsPlayed() {
		return songsPlayed;
	}
	
	public LoadedTrack getTrack(int i) {
		return queue.get(i);
	}
	
	public boolean isShuffling() {
		return isShuffling;
	}
	
	public LoopStatus getLoopStatus() {
		return loopStatus;
	}
	
	public boolean hasPlayNext() {
		return playNextAvaliable;
	}
	
	public LoadedTrack getPlayNext() {
		return playNext;
	}
	
	// Utils
	private void printLoadFailed(FriendlyException e, TextChannel channel) {
		Log.printErr(e);
		TextSender.sendThenWait(channel, "Failed to load song, " + e.getLocalizedMessage());
	}
}
