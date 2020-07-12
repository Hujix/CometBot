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

package codedcosmos.cometbot.guild.chat.messages.built.nowplaying;

import codedcosmos.cometbot.audio.speaker.LoopStatus;
import codedcosmos.cometbot.core.CometBot;
import codedcosmos.cometbot.guild.context.CometGuildContext;
import codedcosmos.cometbot.audio.speaker.MusicSpeaker;
import codedcosmos.cometbot.audio.speaker.SpeakerStatus;
import codedcosmos.cometbot.audio.track.LoadedTrack;
import codedcosmos.cometbot.utils.unicode.UnicodeReactions;
import codedcosmos.hyperdiscord.chat.messages.DynamicMessage;
import codedcosmos.hyperdiscord.chat.reactions.ReactionBox;
import codedcosmos.hyperdiscord.utils.debug.Log;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;

public class NowPlayingMessage extends DynamicMessage {

	// Speaker
	private MusicSpeaker speaker;
	private boolean songHasCompleted = false;

	// Message
	private EmbedBuilder embedBuilder;
	private Message message;
	
	// Song
	private String songLink;
	
	// Reactions
	private ReactionBox playpause;
	private ReactionBox stop;
	private ReactionBox skip;
	private ReactionBox like;
	
	// State
	private boolean sent = false;
	private boolean errored = false;
	
	public NowPlayingMessage(CometGuildContext context) {
		super(context);
		this.speaker = context.getSpeaker();
		embedBuilder = new EmbedBuilder();
	}

	@Override
	public Message getNew() {
		MessageBuilder builder = new MessageBuilder();

		// Get message text
		String messageText = "";

		builder.append(messageText);

		LoadedTrack track = speaker.getCurrentTrack();
		songLink = track.getLink();

		// Update embed builder
		embedBuilder.clearFields();

		// Author
		String author = track.getSongAuthor();
		if (!songHasCompleted) {
			author = "Track: " + speaker.getNowPlayingMessageText() + "\n" + author;
		}
		embedBuilder.setAuthor(author);
		
		// Title
		embedBuilder.setTitle(track.getSongTitle(), track.getLink());
		
		// Embed
		if (track.hasEmbedImage()) {
			embedBuilder.setThumbnail(track.getEmbedImageLink());
		}
		
		// Description
		String description = "";
		if (errored) {
			description += "Error";
		} else if (songHasCompleted) {
			description += "Completed";
		} else {
			description += "**"+speaker.getCurrentTimestamp()+"**";
		}
		
		if (!songHasCompleted) {
			description += "\n";
			
			if (speaker.getStatus() == SpeakerStatus.Playing) {
				description += UnicodeReactions.PLAY + " ";
			} else if (speaker.getStatus() == SpeakerStatus.Paused) {
				description += UnicodeReactions.PAUSE + " ";
			} else {
				description += UnicodeReactions.CHECK_MARK + " ";
			}
			//
			if (speaker.getTrackList().getLoopStatus() == LoopStatus.Loop) {
				description += UnicodeReactions.LOOP + " ";
			} else if (speaker.getTrackList().getLoopStatus() == LoopStatus.LoopSong) {
				description += UnicodeReactions.LOOP_SONG + " ";
			} else { // no loop
				description += UnicodeReactions.NO_LOOP + " ";
			}
			//
			if (speaker.getTrackList().isShuffling()) {
				description += UnicodeReactions.SHUFFLE;
			} else {
				description += UnicodeReactions.NO_SHUFFLE;
			}
		}
		
		embedBuilder.setDescription(description);
		
		// Footer
		embedBuilder.setFooter("Added by " + track.getDJ());
		
		// Build
		message = builder.setEmbed(embedBuilder.build()).build();
		
		return message;
	}
	
	@Override
	public void postSend(Message message) {
		// Reactions
		playpause = new ReactionBox(message, UnicodeReactions.PLAYPAUSE);
		stop = new ReactionBox(message, UnicodeReactions.STOP);
		skip = new ReactionBox(message, UnicodeReactions.NEXT);
		like = new ReactionBox(message, UnicodeReactions.HEART);
		
		// Update sent
		sent = true;
	}
	
	@Override
	public void postUpdate(Message message) {
		if (songHasCompleted || errored) {
			Log.print("Clearing Now Playing Reactions");
			
			// Add slight delay if errored to confirm removal of all reactions
			if (errored) {
				try { Thread.sleep(5000); } catch (InterruptedException e) { Log.printErr(e); }
			}
			
			message.clearReactions().complete();
		}
	}
	
	public void completeSong() {
		if (!sent || songHasCompleted) return;
		
		// Save likes
		int newLikes = Math.min(0, like.getCount()-1);
		speaker.getContext().incrementSongLikes(songLink, newLikes);
		
		// Increment stats
		CometGuildContext context = speaker.getContext();
		context.getStatsRecorder().addLikes(newLikes);
		context.getStatsRecorder().addTracksPlayed(1);
		
		// Print
		Log.print("Added " + newLikes + " to song " + songLink);
		
		// Complete
		completeMessage();
	}
	
	public void forceComplete() {
		if (!songHasCompleted) {
			Log.print("Force completing NowPlaying message");
			completeMessage();
		}
	}
	
	private void completeMessage() {
		this.songHasCompleted = true;
		updateState();
	}

	public boolean hasSongCompleted() {
		return songHasCompleted;
	}
	
	public void setErrored() {
		errored = true;
	}
	
	@Override
	public void onReactionAdd(GuildMessageReactionAddEvent event) {
		if (!sent || errored) return;
		
		playpause.onReactionAdd(event);
		stop.onReactionAdd(event);
		skip.onReactionAdd(event);
		like.onReactionAdd(event);
		
		CometGuildContext context = CometBot.guilds.getContextBy(event.getGuild());
		
		if (playpause.isSelected()) {
			context.getSpeaker().pause(false);
		}
		if (stop.isSelected()) {
			context.getSpeaker().stop(true);
		}
		if (skip.isSelected()) {
			context.getSpeaker().skip();
		}
		if (like.isSelected()) {
		
		}
	}
	
	@Override
	public void onReactionRemove(GuildMessageReactionRemoveEvent event) {
		if (!sent || errored) return;
		
		playpause.onReactionRemove(event);
		stop.onReactionRemove(event);
		skip.onReactionRemove(event);
		like.onReactionRemove(event);
		
		CometGuildContext context = CometBot.guilds.getContextBy(event.getGuild());
		
		if (!playpause.isSelected()) {
			context.getSpeaker().play(false);
		}
		if (!stop.isSelected()) {
		
		}
		if (!skip.isSelected()) {
		
		}
		if (!like.isSelected()) {
		
		}
	}
}
