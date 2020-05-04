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

package codedcosmos.cometbot.audio.track;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class LoadedTrack {
	private AudioTrack track;
	private String link;

	// User who posted track
	private String dj;

	// Embed Image
	private String embedImageLink;
	private boolean hasEmbedImage;

	// Details
	private boolean isLiveStream;
	
	public LoadedTrack(AudioTrack track, String dj, String link) {
		this.track = track;
		this.dj = dj;
		this.link = link;
		this.isLiveStream = track.getInfo().isStream;

		processEmbedImage(link);
	}

	private void processEmbedImage(String link) {
		link = link.replaceAll("https://", "");
		link = link.replaceAll("www.", "");

		if (link.startsWith("soundcloud.com/")) {
			embedImageLink = "https://w.soundcloud.com/icon/assets/images/orange_white_32-94fc761.png";
			hasEmbedImage = true;
		} else if (link.startsWith("youtube.com/watch?v=")) {
			String identifier = link.replaceAll("youtube.com/watch?v=", "");
			identifier = identifier.substring(0, 10);
			embedImageLink = "https://img.youtube.com/vi/"+track.getIdentifier()+"/mqdefault.jpg";
			hasEmbedImage = true;
		} else {
			hasEmbedImage = false;
		}
	}

	public AudioTrack getTrack() {
		return track;
	}

	public String getLink() {
		return link;
	}

	public String getDJ() {
		return dj;
	}

	public String summary() {
		return "LoadedTrack: (" + link + " "  + dj + ")";
	}

	public boolean hasEmbedImage() {
		return hasEmbedImage;
	}

	public String getEmbedImageLink() {
		return embedImageLink;
	}

	public String getSongAuthor() {
		return track.getInfo().author;
	}

	public String getSongTitle() {
		return track.getInfo().title;
	}

	public long getSongLength() {
		return track.getInfo().length;
	}
	
	public boolean isLiveStream() {
		return isLiveStream;
	}
}
