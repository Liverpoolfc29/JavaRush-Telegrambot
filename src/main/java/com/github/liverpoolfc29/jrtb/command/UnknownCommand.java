package com.github.liverpoolfc29.jrtb.command;

import com.github.liverpoolfc29.jrtb.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class UnknownCommand implements Command {
    private final SendBotMessageService sendBotMessageService;

    public final static String UNKNOWN_MESSAGE = "Не понимаю вас ): , напишите /help чтобы узнать что я понимаю.";

    public UnknownCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), UNKNOWN_MESSAGE);
    }

}