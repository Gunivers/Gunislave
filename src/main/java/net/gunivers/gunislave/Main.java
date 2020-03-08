package net.gunivers.gunislave;

import fr.syl2010.utils.io.parser.UnixCommandLineParser;

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

		BOT_INSTANCE.loginBlock(); // lance le bot en bloquant le thread principal
	}

	public static BotInstance getBotInstance() { return BOT_INSTANCE; }
}