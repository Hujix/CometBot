package codedcosmos.cometbot.audio.input;

import codedcosmos.hyperdiscord.utils.debug.Log;
import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.CombinedAudio;
import net.dv8tion.jda.api.audio.UserAudio;
import net.dv8tion.jda.api.audio.hooks.ConnectionListener;
import net.dv8tion.jda.api.audio.hooks.ConnectionStatus;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class AudioReciever implements ConnectionListener {
	@Override
	public void onPing(long ping) {}
	
	@Override
	public void onStatusChange(@Nonnull ConnectionStatus status) {}
	
	@Override
	public void onUserSpeaking(@Nonnull User user, boolean speaking) {
		Log.print("USer speaking " + speaking);
	}
}
