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

import codedcosmos.cometbot.audio.input.AudioReciever;
import codedcosmos.cometbot.audio.speaker.MusicSpeaker;
import codedcosmos.hyperdiscord.guild.GuildContext;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;

public class CometGuildContext extends GuildContext {
	
	// Voice
	private MusicSpeaker speaker;
	private boolean isConnectedToVoice;
	private VoiceChannel voiceChannel;
	
	private AudioReciever reciever;
	
	// Chat
	private DynamicMessages dynamicMessages;
	
	public CometGuildContext(Guild guild) {
		super(guild);
		
		this.speaker = new MusicSpeaker(this);
		reciever = new AudioReciever();
		isConnectedToVoice = false;
		
		dynamicMessages = new DynamicMessages(this);
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
	
	public void disconnectFromVoice() {
		isConnectedToVoice = false;
		guild.getAudioManager().closeAudioConnection();
		speaker.leave();
	}
	
	public boolean isConnectedToVoice() {
		return isConnectedToVoice;
	}
	
	public void setVoiceChannel(VoiceChannel voicechannel) {
		this.voiceChannel = voicechannel;
		isConnectedToVoice = true;
	}
	
	public VoiceChannel getVoiceChannel() {
		return voiceChannel;
	}
	
	public DynamicMessages getDynamicMessages() {
		return dynamicMessages;
	}
	
	public AudioReciever getReciever() {
		return reciever;
	}
}
