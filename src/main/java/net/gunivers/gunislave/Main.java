package net.gunivers.gunislave;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.util.Snowflake;
import fr.syl2010.utils.io.parser.UnixCommandLineParser;
import net.gunivers.gunislave.plugin.PluginManager;

public class Main
{
	private static BotInstance BOT_INSTANCE;

	public static void main(String[] args)
	{
		try
		{
			BOT_INSTANCE = new BotInstance(new BotConfig(new UnixCommandLineParser(args)));
		} catch (Throwable t)
		{
			System.out.println("Could not load configuration, shutting down: " + t.getMessage());
			return;
		}

		BOT_INSTANCE.getBotClient().getEventDispatcher().on(MessageCreateEvent.class)
				.filter(event -> event.getMessage().getContent().filter(string -> string.equals(",stop")).isPresent())
				.subscribe(e -> BOT_INSTANCE.shutdown());

		BOT_INSTANCE.getBotClient().getGuildById(Snowflake.of("379308111774875648"))
				.flatMap(g -> g.getChannelById(Snowflake.of("572008562331746334")))
				.cast(MessageChannel.class)
				.flatMap(c -> c.createMessage("Bot démarré !"))
				.subscribe();

		PluginManager.loadPlugins();

		BOT_INSTANCE.loginBlock(); // lance le bot en bloquant le thread principal

	}

	public static BotInstance getBotInstance() { return BOT_INSTANCE; }
}