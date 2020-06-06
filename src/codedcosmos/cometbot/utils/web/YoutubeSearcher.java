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

package codedcosmos.cometbot.utils.web;

import codedcosmos.hyperdiscord.utils.debug.Log;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class YoutubeSearcher {
	
	public static String youtubeApiKey;
	private static YouTube youtube;
	private static boolean isSetup = false;
	
	public static void setup(String youtubeApiKey) {
		try {
			youtube = new YouTube.Builder(
					GoogleNetHttpTransport.newTrustedTransport(),
					JacksonFactory.getDefaultInstance(),
					null)
					.setApplicationName("CometBot")
					.build();
			YoutubeSearcher.youtubeApiKey = youtubeApiKey;
			isSetup = true;
			
			Log.print("Setup youtube api search");
		} catch (Exception e) {
			Log.printErr("Error Setting up youtube search api");
			Log.printErr(e);
		}
	}
	
	public static ArrayList<SearchTrack> searchForSongs(String searchTerms, int count) throws Exception {
		if (!isSetup) {
			throw new Exception("Youtube Search not configured correctly");
		}
		
		List<SearchResult> results = youtube.search()
				.list("id,snippet")
				.setQ(searchTerms)
				.setMaxResults((long) count)
				.setType("video")
				.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)")
				.setKey(youtubeApiKey)
				.execute()
				.getItems();
		
		ArrayList<SearchTrack> searchTracks = new ArrayList<SearchTrack>(results.size());
		
		for (SearchResult result : results) {
			String link = "www.youtube.com/watch?v="+result.getId().getVideoId();
			String name = result.getSnippet().getTitle();
			
			SearchTrack searchTrack = new SearchTrack(link, name);
			searchTracks.add(searchTrack);
		}
		
		return searchTracks;
	}
}
