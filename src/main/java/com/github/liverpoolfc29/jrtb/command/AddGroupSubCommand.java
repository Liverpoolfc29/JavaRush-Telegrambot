package com.github.liverpoolfc29.jrtb.command;

import com.github.liverpoolfc29.jrtb.javarushclient.JavaRushGroupClient;
import com.github.liverpoolfc29.jrtb.service.GroupSubService;
import com.github.liverpoolfc29.jrtb.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Add Group subscription {@link Command}.
 * Здесь нужно выполнить следующую логику: если нам приходит просто команда, без какого-либо контекста,
 * мы помогаем пользователю и передаем ему список всех групп с их ID-шниками, чтобы он смог передать боту нужную информацию.
 * А если пользователь передает боту команду с еще каким-то словом (словами) — найти группу с таким ID или написать, что такой группы нет.
 */
public class AddGroupSubCommand implements Command {

    private final SendBotMessageService sendBotMessageService;
    private final JavaRushGroupClient javaRushGroupClient;
    private final GroupSubService groupSubService;

    public AddGroupSubCommand(SendBotMessageService sendBotMessageService, JavaRushGroupClient javaRushGroupClient, GroupSubService groupSubService) {
        this.sendBotMessageService = sendBotMessageService;
        this.javaRushGroupClient = javaRushGroupClient;
        this.groupSubService = groupSubService;
    }

    @Override
    public void execute(Update update) {

    }

}
