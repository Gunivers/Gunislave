package net.gunivers.gunislave;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.gunivers.gunislave.data.Database;
import net.gunivers.gunislave.data.DatabaseCredentials;

import fr.syl2010.utils.io.parser.UnixCommandLineParser;
import fr.syl2010.utils.io.parser.UnixConfigParser;

import discord4j.core.object.util.Snowflake;

public class BotConfig
{
	private final String token;
	private final Set<Snowflake> developperIds;
	private final DatabaseCredentials credentials;

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

		if (strDevelopperIds.isEmpty())
			System.out.println("No developper id provided!");
		else
			developperIds.addAll(this.devIdsToSnowflakes(strDevelopperIds));

		this.credentials = this.getCredentials(args, config);
	}

	private Set<Snowflake> devIdsToSnowflakes(String developperIds)
	{
		Set<Snowflake> snowflakes = new HashSet<>();
		String[] ids = developperIds.split(",");

		for (String id : ids)
			snowflakes.add(Snowflake.of(id.trim()));

		return snowflakes;
	}

	private DatabaseCredentials getCredentials(UnixCommandLineParser args, UnixConfigParser config)
	{
		if (!Database.isEnabled())
			return null;

		String user = this.getConfig(args, config, "db-user", "db-user", null);
		String host = this.getConfig(args, config, "db-host", "db-host", null);
		String port = this.getConfig(args, config, "db-port", "db-port", null);
		String password = this.getConfig(args, config, "db-pass", "db-pass", null);
		String database = this.getConfig(args, config, "db-name", "db-name", null);

		return new DatabaseCredentials(user, host, port, password, database);
	}

	private String getConfig(UnixCommandLineParser args, UnixConfigParser config, String argName, String configName, String orElse)
	{
		return args.getDefaultArguments(argName, config.getDefaultArguments(configName, orElse));
	}

	//Should not leak outside of the main package
	String token() { return this.token; }
	DatabaseCredentials credentials() { return this.credentials; }

	public Set<Snowflake> developperIds() { return this.developperIds; }
	public boolean hasToken() { return this.token != null; }
}
