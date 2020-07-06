package net.gunivers.gunislave;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import fr.syl2010.utils.io.parser.UnixCommandLineParser;
import fr.syl2010.utils.io.parser.UnixConfigParser;

import discord4j.core.object.util.Snowflake;

public class BotConfig
{
	private final String token;
	private final Set<Snowflake> developperIds;

	public BotConfig(UnixCommandLineParser args) throws IOException
	{
		File configFile = new File(args.getDefaultArguments("f", "./config"));
		UnixConfigParser config = new UnixConfigParser(configFile);

		//Get token
		this.token = this.getConfig(args, config, "t", "token", "");

		if (this.token.isEmpty())
			throw new IllegalArgumentException("No token provided!");

		Set<Snowflake> developperIds = new HashSet<>();
		this.developperIds = Collections.unmodifiableSet(developperIds);

		//Get developper ids [configuration]
		String strDevelopperIds = this.getConfig(args, config, "dev_ids", "developpers_ids", "");

		if (!strDevelopperIds.isEmpty())
			developperIds.addAll(this.devIdsToSnowflakes(strDevelopperIds));
		else
			System.err.println("No developper id found in either configuration or command line arguments!");
	}

	private String getConfig(UnixCommandLineParser args, UnixConfigParser config, String argName, String configName, String orElse) {
		return args.getDefaultArguments(argName, config.getDefaultArguments(configName, orElse)); }

	private Set<Snowflake> devIdsToSnowflakes(String developperIds)
	{
		Set<Snowflake> snowflakes = new HashSet<>();
		String[] ids = developperIds.split(",");

		for (String id : ids)
			snowflakes.add(Snowflake.of(id.trim()));

		return snowflakes;
	}

	//Should not leak outside of the main package
	String token() { return this.token; }

	public Set<Snowflake> developperIds() { return this.developperIds; }
	public boolean hasToken() { return this.token != null; }
}
