package net.gunivers.gunislave.command.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import fr.theogiraudet.json_command_parser.Command;
import fr.theogiraudet.json_command_parser.CommandExecutor;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

public class ChannelCommand extends Command {

    public List<String> dispChannel(MessageCreateEvent messageCreateEvent) {
        Flux<String> channels = messageCreateEvent.getGuild().flatMapMany(guild -> guild.getChannels().map(channel -> channel.getName()));
        if(!CommandExecutor.isPipedCommands().get())
            messageCreateEvent.getMessage().getChannel()
                .flatMap(message -> message.createMessage(String.join("\n", channels.toIterable())))
                .subscribe();
        return channels.toStream().collect(Collectors.toList());
    }

    @Override
    public String defineSyntaxFile() {
        return "commands/Channel.jc";
    }

}
