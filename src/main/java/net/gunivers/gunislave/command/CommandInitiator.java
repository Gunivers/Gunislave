package net.gunivers.gunislave.command;

import discord4j.core.event.domain.message.MessageCreateEvent;
import fr.theogiraudet.json_command_parser.CommandExecutor;
import fr.theogiraudet.json_command_parser.CommandParser;
import fr.theogiraudet.json_command_parser.Configuration;
import net.gunivers.gunislave.Main;

public class CommandInitiator {

    public static void initialize() {
        // Definition of the command prefix
        Configuration.setPrefix(",");

        // Definition of the package where commands classes are
        CommandParser.parseCommands("net.gunivers.gunislave.command.commands");

        // Listening to commands and then executing them
        Main.getBotInstance().getBotClient().getEventDispatcher().on(MessageCreateEvent.class)
                .filter(msg -> msg.getMessage()
                        .getContent()
                        .orElse("").startsWith(Configuration.getPrefix()))
                .subscribe(msg -> CommandExecutor.execute(msg
                        .getMessage()
                        .getContent()
                        .get()
                        .substring(Configuration.getPrefix().length()), msg));
    }

}
