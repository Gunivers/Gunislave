package net.gunivers.gunislave.command.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import fr.theogiraudet.json_command_parser.Command;
import fr.theogiraudet.json_command_parser.CommandExecutor;

import java.util.List;
import java.util.stream.Collectors;

public class IfCommand extends Command {

    public void ifs(MessageCreateEvent event, Boolean condition, List<String> command) {
        if(condition)
            new CommandExecutor().execute(command.stream().collect(Collectors.joining(" ")), event);
    }

    public void ifElse(MessageCreateEvent event, Boolean condition, List<String> command , List<String> command2) {
        System.out.println(condition);
        if(condition)
            new CommandExecutor().execute(command.stream().collect(Collectors.joining(" ")), event);
        else
            new CommandExecutor().execute(command2.stream().collect(Collectors.joining(" ")), event);
    }

    @Override
    public String defineSyntaxFile() {
        return "commands/If.jc";
    }

}
