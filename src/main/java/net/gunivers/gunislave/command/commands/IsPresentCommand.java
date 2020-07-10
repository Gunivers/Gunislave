package net.gunivers.gunislave.command.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.presence.Status;
import fr.theogiraudet.json_command_parser.Command;

public class IsPresentCommand extends Command {

    public boolean isPresent(MessageCreateEvent event, String user) {
        return event.getGuild().flatMapMany(guild ->guild.getMembers())
                .filter(u -> u.getMention().equals(user.replace("!", "")))
                .flatMap(u -> u.getPresence())
                .any(p -> p.getStatus() != Status.OFFLINE).block();
    }

    @Override
    public String defineSyntaxFile() {
        return "commands/IsPresent.jc";
    }

}
