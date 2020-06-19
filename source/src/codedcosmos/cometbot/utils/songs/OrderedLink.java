package codedcosmos.cometbot.utils.songs;

public class OrderedLink {
	private String link;
	private int likes;
	
	public OrderedLink(String link, int likes) {
		this.link = link;
		this.likes = likes;
	}
	
	public String getLink() {
		return link;
	}
	
	public int getLikes() {
		return likes;
	}
}
