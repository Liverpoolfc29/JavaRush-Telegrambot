package com.github.liverpoolfc29.jrtb.command;

import com.github.liverpoolfc29.jrtb.service.SendBotMessageService;
import com.google.common.collect.ImmutableMap;

import static com.github.liverpoolfc29.jrtb.command.CommandName.*;

public class CommandContainer {

    private final ImmutableMap<Object, Object> commandImmutableMap;
    private final Command unknownCommand;

    public CommandContainer(SendBotMessageService sendBotMessageService) {
        commandImmutableMap = ImmutableMap.builder()
                .put(START.getCommandName(), new StartCommand(sendBotMessageService))
                .put(STOP.getCommandName(), new StopCommand(sendBotMessageService))
                .put(HELP.getCommandName(), new HelpCommand(sendBotMessageService))
                .put(NO.getCommandName(), new NoCommand(sendBotMessageService))
                .build();

        unknownCommand = new UnknownCommand(sendBotMessageService);
    }

    public Command retrieveCommand(String commandIdentifier) {
        return (Command) commandImmutableMap.getOrDefault(commandIdentifier, unknownCommand);
    }

}
