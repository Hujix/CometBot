package codedcosmos.cometbot.guild.commands;

import codedcosmos.cometbot.core.CometBot;
import codedcosmos.cometbot.utils.system.RuntimeDetails;
import codedcosmos.hyperdiscord.chat.TextSender;
import codedcosmos.hyperdiscord.command.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class RuntimeStatus implements Command {
	@Override
	public String getName() {
		return "runtimestatus";
	}
	
	@Override
	public String getHelp() {
		return "Gets current details about the JVM";
	}
	
	@Override
	public String[] getStynax() {
		return new String[] {""};
	}
	
	@Override
	public void run(MessageReceivedEvent event) throws Exception {
		MessageBuilder builder = new MessageBuilder();
		EmbedBuilder embedBuilder = new EmbedBuilder();
		
		embedBuilder.setTitle("Runtime Status");
		embedBuilder.setFooter("CometBot v" + CometBot.VERSION);
		
		String description = "";
		
		description += "**System Properties**\n";
		description += "Java Version-----: " + System.getProperty("java.version") + "\n";
		description += "OS Arch ---------: " + System.getProperty("os.arch") + "\n";
		description += "OS Name -------: " + System.getProperty("os.name") + "\n";
		description += "OS Version -----: " + System.getProperty("os.version") + "\n";
		
		description += "\n";
		
		description += "**Memory Usage**\n";
		description += "Heap Max-------: " + RuntimeDetails.getHeapMaxSize() + "MB\n";
		description += "Heap-------------: " + RuntimeDetails.getHeapSize() + "MB\n";
		description += "Heap Free-------: " + RuntimeDetails.getHeapFreeSize() + "MB\n";
		description += "Heap Usage-----: " + RuntimeDetails.getHeapUsage() + "MB\n";
		
		embedBuilder.setDescription(description);
		
		Message message = builder.setEmbed(embedBuilder.build()).build();
		TextSender.send(event, message);
	}
	
	@Override
	public String[] getAliases() {
		return new String[] {"jvm"};
	}
}
