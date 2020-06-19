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

import codedcosmos.cometbot.guild.chat.messages.built.help.HelpMessage;
import codedcosmos.cometbot.guild.chat.messages.built.nowplaying.NowPlayingMessage;
import codedcosmos.cometbot.guild.chat.messages.built.queue.QueueMessage;
import codedcosmos.cometbot.guild.chat.messages.built.search.SearchMessage;
import codedcosmos.hyperdiscord.chat.reactions.ReactionReactor;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;

public class DynamicMessages implements ReactionReactor {
	
	private CometGuildContext context;
	
	public DynamicMessages(CometGuildContext context) {
		this.context = context;
		
		searchMessage = new SearchMessage(context);
		nowPlayingMessage = new NowPlayingMessage(context);
		helpMessage = new HelpMessage(context, "1");
		queueMessage = new QueueMessage(context);
	}
	
	public void onMessageDelete(MessageDeleteEvent event) {
		searchMessage.checkForDeletion(event);
		nowPlayingMessage.checkForDeletion(event);
		helpMessage.checkForDeletion(event);
		queueMessage.checkForDeletion(event);
	}
	
	
	// Reactions
	@Override
	public void onReactionAdd(GuildMessageReactionAddEvent event) {
		nowPlayingMessage.onReactionAdd(event);
		helpMessage.onReactionAdd(event);
		queueMessage.onReactionAdd(event);
		searchMessage.onReactionAdd(event);
	}
	
	@Override
	public void onReactionRemove(GuildMessageReactionRemoveEvent event) {
		nowPlayingMessage.onReactionRemove(event);
		helpMessage.onReactionRemove(event);
		queueMessage.onReactionRemove(event);
		searchMessage.onReactionRemove(event);
	}
	
	
	// Search message
	private SearchMessage searchMessage;
	
	public void forceCompleteSearchMessage() {
		searchMessage.forceComplete();
	}
	
	public void sendSearchMessage(String search) {
		searchMessage.forceComplete();
		searchMessage = new SearchMessage(context);
		searchMessage.sendSearch(search);
	}
	
	// Now playing message
	private NowPlayingMessage nowPlayingMessage;
	
	public void sendNowPlayingMessage() {
		nowPlayingMessage.send();
	}
	
	public void completeNowPlayingMessagesSong() {
		nowPlayingMessage.completeSong();
		nowPlayingMessage = new NowPlayingMessage(context);
	}
	
	public void completeNowPlayingMessagesSongAsError() {
		nowPlayingMessage.setErrored();
		nowPlayingMessage.forceComplete();
		nowPlayingMessage = new NowPlayingMessage(context);
	}
	
	public void updateNowPlayingState() {
		if (nowPlayingMessage.hasSongCompleted()) return;
		
		nowPlayingMessage.updateState();
	}
	
	
	
	// Help
	private HelpMessage helpMessage;
	
	public void forceCompleteHelpMessage() {
		helpMessage.clearReactions();
	}
	
	public void sendHelpMessage(String content) {
		helpMessage.clearReactions();
		helpMessage = new HelpMessage(context, content);
		helpMessage.send();
	}
	
	
	
	// Queue
	private QueueMessage queueMessage;
	
	public void forceCompleteQueueMessage() {
		queueMessage.clearReactions();
	}
	
	public void sendQueueMessage() {
		queueMessage.clearReactions();
		queueMessage = new QueueMessage(context);
		queueMessage.send();
	}
}
