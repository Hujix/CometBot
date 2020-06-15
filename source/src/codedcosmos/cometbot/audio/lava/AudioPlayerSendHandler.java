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

package codedcosmos.cometbot.audio.lava;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;

import java.nio.Buffer;
import java.nio.ByteBuffer;

public class AudioPlayerSendHandler implements AudioSendHandler {
	private final AudioPlayer audioPlayer;
	private final ByteBuffer buffer;
	private final MutableAudioFrame frame;

	/**
	 * @param audioPlayer Audio player to wrap.
	 */
	public AudioPlayerSendHandler(AudioPlayer audioPlayer) {
		this.audioPlayer = audioPlayer;
		this.audioPlayer.setFrameBufferDuration(500);
		this.buffer = ByteBuffer.allocate(1024);
		this.frame = new MutableAudioFrame();
		this.frame.setBuffer(buffer);
	}

	@Override
	public boolean canProvide() {
		// returns true if audio was provided
		return audioPlayer.provide(frame);
	}

	@Override
	public ByteBuffer provide20MsAudio() {
		// flip to make it a read buffer
		((Buffer) buffer).flip();
		return buffer;
	}

	@Override
	public boolean isOpus() {
		return true;
	}
}
