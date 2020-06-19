package codedcosmos.cometbot.utils.web;

public class LinkConverter {
	public static String makeLinkRigid(String link) {
		link = link.replaceAll("https://", "");
		link = link.replaceAll("/", "-");
		link = link.replaceAll("=", "");
		
		return link;
	}
}
