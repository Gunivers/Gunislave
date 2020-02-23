package net.gunivers.gunislave;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import discord4j.core.object.util.Snowflake;
import fr.syl2010.utils.io.parser.UnixCommandLineParser;
import fr.syl2010.utils.io.parser.UnixConfigParser;

public class BotConfig {

    public static final String SQL_URL_FORMAT = "jdbc:mysql://%s/%s?serverTimezone=Europe/Paris&autoReconnect=true&failOverReadOnly=false&maxReconnects=3";

    public final String token;
    public final List<Snowflake> developperIds;

    public BotConfig(UnixCommandLineParser argParser) {
	File confFile = new File(argParser.getDefaultArguments("f", "./config"));
	UnixConfigParser config;
	try {
	    config = new UnixConfigParser(confFile);
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
	developperIds = new ArrayList<>();

	token = argParser.getDefaultArguments("t", config.getDefaultArguments("token", ""));
	if (token.isEmpty())
	    throw new IllegalArgumentException("Aucun token n'as été donné !");

	String strDevelopperIds = config.getDefaultArguments("developpers_ids", "");
	if (!strDevelopperIds.isEmpty()) {
	    developperIds.addAll(Arrays.asList(strDevelopperIds.split(",")).stream().map(Snowflake::of)
		    .collect(Collectors.toList()));
	}
	strDevelopperIds = argParser.getDefaultArguments("dev_ids", "");
	if (!strDevelopperIds.isEmpty()) {
	    developperIds.addAll(Arrays.asList(strDevelopperIds.split(",")).stream().map(Snowflake::of)
		    .collect(Collectors.toList()));
	}
    }

}
