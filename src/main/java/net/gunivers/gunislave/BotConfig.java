package net.gunivers.gunislave;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import fr.syl2010.utils.io.parser.UnixCommandLineParser;
import fr.syl2010.utils.io.parser.UnixConfigParser;

import discord4j.core.object.util.Snowflake;

public class BotConfig
{
	public static final String SQL_URL_FORMAT = "jdbc:mysql://%s/%s?serverTimezone=Europe/Paris&autoReconnect=true&failOverReadOnly=false&maxReconnects=3";

	public final String token;
	public final List<Snowflake> developperIds;

	public BotConfig(UnixCommandLineParser argParser) throws IOException
	{
		this.developperIds = new ArrayList<>();

		File configFile = new File(argParser.getDefaultArguments("f", "./config"));
		UnixConfigParser config = new UnixConfigParser(configFile);

		//Get token
		this.token = argParser.getDefaultArguments("t", config.getDefaultArguments("token", ""));

		if (this.token.isEmpty())
			throw new IllegalArgumentException("No token provided!");

		//Get developper ids [configuration]
		String strDevelopperIds = config.getDefaultArguments("developpers_ids", "");

		if (!strDevelopperIds.isEmpty())
			this.developperIds.addAll(Arrays.asList(strDevelopperIds.split(",")).stream().map(Snowflake::of).collect(Collectors.toList()));
		else
			System.out.println("No developper id found in configuration!");

		//Get developper ids [command line]
		strDevelopperIds = argParser.getDefaultArguments("dev_ids", "");

		if (!strDevelopperIds.isEmpty())
			this.developperIds.addAll(Arrays.asList(strDevelopperIds.split(",")).stream().map(Snowflake::of).collect(Collectors.toList()));
		else
			System.out.println("No developper id provided in command line!");
	}
}
