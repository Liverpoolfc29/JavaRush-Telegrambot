package com.github.liverpoolfc29.jrtb.command;

import com.github.liverpoolfc29.jrtb.command.annotation.AdminCommand;
import com.github.liverpoolfc29.jrtb.javarushclient.JavaRushGroupClient;
import com.github.liverpoolfc29.jrtb.service.GroupSubService;
import com.github.liverpoolfc29.jrtb.service.SendBotMessageService;
import com.github.liverpoolfc29.jrtb.service.TelegramUserService;
import com.google.common.collect.ImmutableMap;

import java.util.List;

import static com.github.liverpoolfc29.jrtb.command.CommandName.*;
import static java.util.Objects.nonNull;

public class CommandContainer {

    private final ImmutableMap<Object, Object> commandImmutableMap;
    private final Command unknownCommand;
    private final List<String> admins;

    public CommandContainer(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService,
                            JavaRushGroupClient javaRushGroupClient, GroupSubService groupSubService,
                            List<String> admins) {

        commandImmutableMap = ImmutableMap.builder()
                .put(START.getCommandName(), new StartCommand(sendBotMessageService, telegramUserService))
                .put(STOP.getCommandName(), new StopCommand(sendBotMessageService, telegramUserService))
                .put(HELP.getCommandName(), new HelpCommand(sendBotMessageService))
                .put(NO.getCommandName(), new NoCommand(sendBotMessageService))
                .put(STAT.getCommandName(), new StatCommand(telegramUserService, sendBotMessageService))
                .put(ADD_GROUP_SUB.getCommandName(), new AddGroupSubCommand(sendBotMessageService, javaRushGroupClient, groupSubService))
                .put(LIST_GROUP_SUB.getCommandName(), new ListGroupSubCommand(sendBotMessageService, telegramUserService))
                .put(DELETE_GROUP_SUB.getCommandName(), new DeleteGroupSubCommand(sendBotMessageService, telegramUserService, groupSubService))
                .put(ADMIN_HELP.getCommandName(), new AdminHelpCommand(sendBotMessageService))
                .build();

        unknownCommand = new UnknownCommand(sendBotMessageService);
        this.admins = admins;
    }

    public Command retrieveCommand(String commandIdentifier, String userName) {
        Command orDefault = (Command) commandImmutableMap.getOrDefault(commandIdentifier, unknownCommand);

        if (isAdminComand(orDefault)) {
            if (admins.contains(userName)) {
                return orDefault;
            } else {
                return unknownCommand;
            }
        }

        return (Command) commandImmutableMap.getOrDefault(commandIdentifier, unknownCommand);
    }

    private boolean isAdminComand(Command command) {
        return nonNull(command.getClass().getAnnotation(AdminCommand.class));
    }

}
