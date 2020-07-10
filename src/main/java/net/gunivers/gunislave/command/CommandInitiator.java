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

		Main.getBotInstance().getBotClient().getEventDispatcher()
			.on(MessageCreateEvent.class)
			.doOnNext(event -> System.out.println(event.getMessage().getContent().get()))
			.subscribe();

		final CommandExecutor executor = new CommandExecutor();

		// Listening to commands and then executing them
		Main.getBotInstance().getBotClient().getEventDispatcher()
			.on(MessageCreateEvent.class)
			.flatMap
			(
				event -> Mono
					.justOrEmpty(event.getMessage().getContent())
					.filter(msg -> msg.startsWith(Configuration.getPrefix()))
					.flatMap
					(
						msg -> Mono.justOrEmpty(executor.execute
						(
							msg.substring(Configuration.getPrefix().length()),
							event
						))
					)
			)
			.subscribe();
	}
}
