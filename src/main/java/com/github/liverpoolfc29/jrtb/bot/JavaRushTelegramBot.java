package com.github.liverpoolfc29.jrtb.bot;

import com.github.liverpoolfc29.jrtb.command.CommandContainer;
import com.github.liverpoolfc29.jrtb.javarushclient.JavaRushGroupClient;
import com.github.liverpoolfc29.jrtb.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.github.liverpoolfc29.jrtb.command.CommandName.NO;

@Component
public class JavaRushTelegramBot extends TelegramLongPollingBot {

    public static String COMMAND_PREFIX = "/";
    @Value("${bot.username}")
    private String userName;
    @Value("${bot.token}")
    private String token;


    private final CommandContainer commandContainer;

    //Мы передаем в виде аргумента TelegramUserService, добавляя аннотацию Autowired. Это значит, что ее мы получим из Application Context.

    @Autowired
    public JavaRushTelegramBot(TelegramUserService telegramUserService, StatisticsService statisticsService, JavaRushGroupClient javaRushGroupClient,
                               GroupSubService groupSubService, @Value("#{'${bot.admins}'.split(',')}") List<String> admins) {

        this.commandContainer = new CommandContainer(new SendBotMessageServiceImpl(this),
                telegramUserService, statisticsService, javaRushGroupClient, groupSubService, admins);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText().trim();
            String userName = update.getMessage().getFrom().getUserName();

            if (message.startsWith(COMMAND_PREFIX)) {
                String commandIdentifier = message.split(" ")[0].toLowerCase();

                commandContainer.findCommand(commandIdentifier, userName).execute(update);
            } else {
                commandContainer.findCommand(NO.getCommandName(), userName).execute(update);
            }

/*
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(message);

            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
 */

        }
    }

    @Override
    public String getBotUsername() {
        return userName;
    }

    @Override
    public String getBotToken() {
        return token;
    }

}
