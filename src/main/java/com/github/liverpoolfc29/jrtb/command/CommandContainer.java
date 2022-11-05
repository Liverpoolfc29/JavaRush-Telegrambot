package com.github.liverpoolfc29.jrtb.command;

import com.github.liverpoolfc29.jrtb.javarushclient.JavaRushGroupClient;
import com.github.liverpoolfc29.jrtb.service.GroupSubService;
import com.github.liverpoolfc29.jrtb.service.SendBotMessageService;
import com.github.liverpoolfc29.jrtb.service.TelegramUserService;
import com.google.common.collect.ImmutableMap;

import static com.github.liverpoolfc29.jrtb.command.CommandName.*;

public class CommandContainer {

    private final ImmutableMap<Object, Object> commandImmutableMap;
    private final Command unknownCommand;

    public CommandContainer(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService, JavaRushGroupClient javaRushGroupClient, GroupSubService groupSubService) {
        commandImmutableMap = ImmutableMap.builder()
                .put(START.getCommandName(), new StartCommand(sendBotMessageService, telegramUserService))
                .put(STOP.getCommandName(), new StopCommand(sendBotMessageService, telegramUserService))
                .put(HELP.getCommandName(), new HelpCommand(sendBotMessageService))
                .put(NO.getCommandName(), new NoCommand(sendBotMessageService))
                .put(STAT.getCommandName(), new StatCommand(telegramUserService, sendBotMessageService))
                .put(ADD_GROUP_SUB.getCommandName(), new AddGroupSubCommand(sendBotMessageService, javaRushGroupClient, groupSubService))
                .put(LIST_GROUP_SUB.getCommandName(), new ListGroupSubCommand(sendBotMessageService, telegramUserService))
                .put(DELETE_GROUP_SUB.getCommandName(), new DeleteGroupSubCommand(sendBotMessageService, telegramUserService, groupSubService))
                .build();

        unknownCommand = new UnknownCommand(sendBotMessageService);
    }

    public Command retrieveCommand(String commandIdentifier) {
        return (Command) commandImmutableMap.getOrDefault(commandIdentifier, unknownCommand);
    }

}
