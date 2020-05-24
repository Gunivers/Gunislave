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
	public static final String SQL_URL_FORMAT = "jdbc:mysql://%s/%s?serverTimezone=Europe/Paris&autoReconnect=true&failOverReadOnly=false&maxReconnects=3";

	private final String token;
	private final Set<Snowflake> developperIdsImmutable;

	public BotConfig(UnixCommandLineParser argParser) throws IOException
	{
		File configFile = new File(argParser.getDefaultArguments("f", "./config"));
		UnixConfigParser config = new UnixConfigParser(configFile);

		//Get token
		this.token = this.getConfig(argParser, config, "t", "token", "");

		if (this.token.isEmpty())
			throw new IllegalArgumentException("No token provided!");


		Set<Snowflake> developperIds = new HashSet<>();
		this.developperIdsImmutable = Collections.unmodifiableSet(developperIds);

		//Get developper ids [configuration]
		String strDevelopperIds = config.getDefaultArguments("developpers_ids", "");

		if (!strDevelopperIds.isEmpty())
			developperIds.addAll(this.devIdsToSnowflakes(strDevelopperIds));
		else
			System.out.println("No developper id found in configuration!");

		//Get developper ids [command line]
		strDevelopperIds = argParser.getDefaultArguments("dev_ids", "");

		if (!strDevelopperIds.isEmpty())
			developperIds.addAll(this.devIdsToSnowflakes(strDevelopperIds));
		else
			System.out.println("No developper id provided in command line!");
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

	public String getToken() { return this.token; }
	public Set<Snowflake> getDevelopperIds() { return this.developperIdsImmutable; }
}
