package com.github.liverpoolfc29.jrtb.command;

import com.github.liverpoolfc29.jrtb.command.annotation.AdminCommand;
import com.github.liverpoolfc29.jrtb.service.SendBotMessageService;
import com.github.liverpoolfc29.jrtb.service.TelegramUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

@AdminCommand
public class StatCommand implements Command {

    private final TelegramUserService telegramUserService;
    private final SendBotMessageService sendBotMessageService;

    public final static String STAT_MESSAGE = "Javarush Telegram Bot использует %s человек.";

    @Autowired
    public StatCommand(TelegramUserService telegramUserService, SendBotMessageService sendBotMessageService) {
        this.telegramUserService = telegramUserService;
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        // мы получаем список всех активных пользователей при помощи метода retrieveAllActiveUsers и получаем размер коллекции.
        int activeUserCount = telegramUserService.retrieveAllActiveUsers().size();
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), String.format(STAT_MESSAGE, activeUserCount));
    }

}
