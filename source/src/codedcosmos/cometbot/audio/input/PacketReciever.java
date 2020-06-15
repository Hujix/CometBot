package codedcosmos.cometbot.audio.input;

import codedcosmos.hyperdiscord.utils.debug.Log;
import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.CombinedAudio;
import net.dv8tion.jda.api.audio.OpusPacket;

import javax.annotation.Nonnull;

public class PacketReciever implements AudioReceiveHandler {
	
	// Time when the last packet was recieved
	private long lastPacketTime;
	
	// State
	private boolean audioIsBeingPlayed;
	
	public PacketReciever() {
		lastPacketTime = System.currentTimeMillis();
		audioIsBeingPlayed = false;
	}
	
	@Override
	public boolean canReceiveCombined() {
		return false;
	}
	
	@Override
	public boolean canReceiveUser() {
		return false;
	}
	
	@Override
	public boolean canReceiveEncoded() {
		return true;
	}
	
	@Override
	public void handleEncodedAudio(@Nonnull OpusPacket packet) {
		lastPacketTime = System.currentTimeMillis();
	}
	
	public void tick100ms() {
		audioIsBeingPlayed = (!(System.currentTimeMillis() > lastPacketTime + 500));
	}
	
	public boolean isAudioIsBeingPlayed() {
		return audioIsBeingPlayed;
	}
}
