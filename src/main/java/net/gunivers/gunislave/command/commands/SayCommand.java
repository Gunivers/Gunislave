package net.gunivers.gunislave.command.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import fr.theogiraudet.json_command_parser.Command;

import java.util.List;

public class SayCommand extends Command {

    public void says(MessageCreateEvent messageCreateEvent, List<String> text) {
        messageCreateEvent.getMessage().getChannel()
                .flatMap(message -> message.createMessage(String.join(" ", text)))
                .subscribe();
    }

    @Override
    public String defineSyntaxFile() {
        return "commands/Say.jc";
    }

}
