package net.gunivers.gunislave.command.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import fr.theogiraudet.json_command_parser.Command;

public class NotCommand extends Command {

    public Boolean negate(MessageCreateEvent event, Boolean b) {
        return !b;
    }

    @Override
    public String defineSyntaxFile() {
        return "commands/Not.jc";
    }

}
