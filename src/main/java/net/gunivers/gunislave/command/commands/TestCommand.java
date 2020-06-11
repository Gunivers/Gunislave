package net.gunivers.gunislave.command.commands;

import fr.theogiraudet.json_command_parser.Command;

public class TestCommand extends Command {

    @Override
    public String defineSyntaxFile() {
        return "commands/Test.jc";
    }
}
