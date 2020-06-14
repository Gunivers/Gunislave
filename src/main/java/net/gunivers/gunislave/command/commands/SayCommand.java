package net.gunivers.gunislave.command.commands;

import java.util.List;

import fr.theogiraudet.json_command_parser.Command;

import discord4j.core.event.domain.message.MessageCreateEvent;

public class SayCommand extends Command
{
	public void says(MessageCreateEvent event, List<String> text)
	{
		event
			.getMessage()
			.getChannel()
			.flatMap(message -> message.createMessage(String.join(" ", text)))
			.subscribe();
	}

	@Override public String defineSyntaxFile() { return "commands/Say.jc"; }
}
