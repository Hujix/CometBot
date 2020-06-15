import codedcosmos.cometbot.guild.chat.messages.CometCommandListener;
import codedcosmos.hyperdiscord.utils.text.OrderedString;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.fail;

public class AliasConflictTest {
	@Test
	public void aliasConflictCheck() {
		CometCommandListener cmdListener = new CometCommandListener();
		ArrayList<OrderedString> suggestions = cmdListener.getSuggestions("a");
		
		for (OrderedString a : suggestions) {
			for (OrderedString b : suggestions) {
				int i = 0;
				if (a.getCompareText().equals(b.getCompareText())) {
					i++;
				}
				if (i > 1) {
					fail("Alias Conflict found with " + a.getCompareText());
				}
			}
		}
	}
}
