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
	
	// Reactions
	private ReactionBox pause;
	private ReactionBox stop;
	private ReactionBox skip;
	private ReactionBox like;
	
	private boolean sent = false;

	public NowPlayingMessage(CometGuildContext context) {
		super(context);
		this.speaker = context.getSpeaker();
		embedBuilder = new EmbedBuilder();
	}

	@Override
	public Message getNew() {
		MessageBuilder builder = new MessageBuilder();

		// Get message text
		String messageText = speaker.getNowPlayingMessageText();

		builder.append(messageText);

		LoadedTrack track = speaker.getCurrentTrack();

		// Update embed builder
		embedBuilder.clearFields();

		embedBuilder.setAuthor(track.getSongAuthor());
		embedBuilder.setTitle(track.getSongTitle(), track.getLink());
		if (track.hasEmbedImage()) {
			embedBuilder.setThumbnail(track.getEmbedImageLink());
		}
		embedBuilder.setFooter("Added by " + track.getDJ());
		if (songHasCompleted) {
			embedBuilder.setDescription("Completed");
		} else if (speaker.getStatus() == SpeakerStatus.Paused) {
			embedBuilder.setDescription("Paused");
		} else {
			embedBuilder.setDescription(speaker.getCurrentTimestamp());
		}
		
		message = builder.setEmbed(embedBuilder.build()).build();
		
		return message;
	}
	
	@Override
	public void postSend(Message message) {
		// Reactions
		pause = new ReactionBox(message, UnicodeReactions.PAUSE);
		stop = new ReactionBox(message, UnicodeReactions.STOP);
		skip = new ReactionBox(message, UnicodeReactions.NEXT);
		like = new ReactionBox(message, UnicodeReactions.HEART);
		
		// Update sent
		sent = true;
	}
	
	@Override
	public void postUpdate(Message message) {
		if (songHasCompleted) {
			message.clearReactions().complete();
			Log.print("Clearing Now Playing Reactions");
		}
	}
	
	public void completeSong() {
		this.songHasCompleted = true;
		updateState();
	}

	public boolean hasSongCompleted() {
		return songHasCompleted;
	}
	
	@Override
	public void onReactionAdd(GuildMessageReactionAddEvent event) {
		if (!sent) return;
		
		pause.onReactionAdd(event);
		stop.onReactionAdd(event);
		skip.onReactionAdd(event);
		like.onReactionAdd(event);
		
		CometGuildContext context = CometBot.guilds.getContextBy(event.getGuild());
		
		if (pause.isSelected()) {
			context.getSpeaker().pause();
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
		if (!sent) return;
		
		pause.onReactionRemove(event);
		stop.onReactionRemove(event);
		skip.onReactionRemove(event);
		like.onReactionRemove(event);
		
		CometGuildContext context = CometBot.guilds.getContextBy(event.getGuild());
		
		if (!pause.isSelected()) {
			context.getSpeaker().play(true);
		}
		if (!stop.isSelected()) {
		
		}
		if (!skip.isSelected()) {
		
		}
		if (!like.isSelected()) {
		
		}
	}
}
