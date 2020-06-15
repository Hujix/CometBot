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

import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class MusicPlayer {

	private static AudioPlayerManager manager;

	public static void init() {
		AudioConfiguration configuration = new AudioConfiguration();
		configuration.setOpusEncodingQuality(AudioConfiguration.OPUS_QUALITY_MAX);
		configuration.setResamplingQuality(AudioConfiguration.ResamplingQuality.HIGH);
		configuration.setFilterHotSwapEnabled(true);

		manager = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(manager);
		AudioSourceManagers.registerLocalSource(manager);
	}

	public static AudioPlayer generatePlayer() {
		AudioPlayer player = manager.createPlayer();
		return player;
	}

	public static AudioPlayerManager getPlayerManager() {
		return manager;
	}
}
