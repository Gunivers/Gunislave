package net.gunivers.gunislave.command.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import fr.theogiraudet.json_command_parser.Command;
import net.gunivers.gunislave.Main;

public class StopCommand extends Command {

    public void stop(MessageCreateEvent e) {
        e.getMessage().getChannel().flatMap(messageChannel -> messageChannel.createMessage("Extinction du bot.")).subscribe();
        Main.getBotInstance().shutdown();
    }

    @Override
    public String defineSyntaxFile() {
        return "commands/Stop.jc";
    }
}
