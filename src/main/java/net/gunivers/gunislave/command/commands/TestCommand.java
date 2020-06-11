package net.gunivers.gunislave.command.commands;

import fr.theogiraudet.json_command_parser.Command;
import fr.theogiraudet.json_command_parser.Ignore;

@Ignore
public class TestCommand extends Command {

    @Override
    public String defineSyntaxFile() {
        return "commands/Test.jc";
    }
}
