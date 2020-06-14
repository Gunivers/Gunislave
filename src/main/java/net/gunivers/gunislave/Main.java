package net.gunivers.gunislave;

import net.gunivers.gunislave.command.CommandInitiator;
import net.gunivers.gunislave.plugin.PluginManager;

import fr.syl2010.utils.io.parser.UnixCommandLineParser;

import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.util.Snowflake;

import java.util.Arrays;

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
			System.out.println(t.getClass().getName());
			System.out.println("Could not load configuration, shutting down: " + t.getMessage());
			return;
		}

		CommandInitiator.initialize();

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