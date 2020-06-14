package net.gunivers.gunislave.command.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import fr.theogiraudet.json_command_parser.Command;
import fr.theogiraudet.json_command_parser.CommandExecutor;

import java.util.Comparator;
import java.util.List;

public class SortCommand extends Command {

    public List<String> sorts(MessageCreateEvent messageCreateEvent, List<String> text) {
        text.sort(Comparator.naturalOrder());
        System.out.println(CommandExecutor.isPipedCommands().get());
        if(!CommandExecutor.isPipedCommands().get())
            messageCreateEvent.getMessage().getChannel()
                .flatMap(message -> message.createMessage(String.join("\n", text)))
                .subscribe();
        return text;
    }

    @Override
    public String defineSyntaxFile() {
        return "commands/Sort.jc";
    }

}
