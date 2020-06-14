package net.gunivers.gunislave.command.commands;

import net.gunivers.gunislave.Main;

import fr.theogiraudet.json_command_parser.Command;

import discord4j.core.event.domain.message.MessageCreateEvent;

public class StopCommand extends Command
{
	public void stop(MessageCreateEvent event)
	{
		event
			.getMessage()
			.getChannel()
			.flatMap(messageChannel -> messageChannel.createMessage("Extinction du bot."))
			.subscribe();

		Main.getBotInstance().shutdown().subscribe();
	}

	@Override public String defineSyntaxFile() { return "commands/Stop.jc"; }
}
