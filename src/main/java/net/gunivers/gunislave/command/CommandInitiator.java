package net.gunivers.gunislave.command;

import net.gunivers.gunislave.Main;

import fr.theogiraudet.json_command_parser.CommandExecutor;
import fr.theogiraudet.json_command_parser.CommandParser;
import fr.theogiraudet.json_command_parser.Configuration;

import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

public class CommandInitiator
{
	public static void initialize()
	{
		// Definition of the command prefix
		Configuration.setPrefix(",");
		Configuration.setDebug(true);

		// Definition of the package where commands classes are
		CommandParser.parseCommands("net.gunivers.gunislave.command.commands");

		// Listening to commands and then executing them
		Main.getBotInstance().getBotClient().getEventDispatcher()
			.on(MessageCreateEvent.class)
			.flatMap
			(
				event -> Mono
					.justOrEmpty(event.getMessage().getContent())
					.flatMap
					(
						msg -> Mono.justOrEmpty(CommandExecutor.isCommand(msg).map(cmd -> CommandExecutor.execute(cmd, event))
						)
					)
			)
			.subscribe();
	}
}
