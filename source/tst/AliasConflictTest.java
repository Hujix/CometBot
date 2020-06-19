import codedcosmos.cometbot.guild.chat.messages.CometCommandListener;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.fail;

public class AliasConflictTest {
	@Test
	public void aliasConflictCheck() {
		CometCommandListener cmdListener = new CometCommandListener();
		ArrayList<String> aliases = cmdListener.getAliases();
		
		for (String a : aliases) {
			int i = 0;
			for (String b : aliases) {
				if (a.equals(b)) {
					i++;
				}
				if (i > 1) {
					fail("Alias Conflict found with " + a);
				}
			}
		}
	}
}
