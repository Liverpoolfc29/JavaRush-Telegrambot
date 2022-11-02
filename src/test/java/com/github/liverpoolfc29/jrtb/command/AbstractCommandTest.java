package com.github.liverpoolfc29.jrtb.command;

import com.github.liverpoolfc29.jrtb.bot.JavaRushTelegramBot;
import com.github.liverpoolfc29.jrtb.service.SendBotMessageService;
import com.github.liverpoolfc29.jrtb.service.SendBotMessageServiceImpl;
import com.github.liverpoolfc29.jrtb.service.TelegramUserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

abstract class AbstractCommandTest {

    protected JavaRushTelegramBot javaRushTelegramBot = Mockito.mock(JavaRushTelegramBot.class);
    protected TelegramUserService telegramUserService = Mockito.mock(TelegramUserService.class);
    protected SendBotMessageService sendBotMessageService = new SendBotMessageServiceImpl(javaRushTelegramBot);

    abstract String getCommandName();

    abstract String getCommandMessage();

    abstract Command getCommand();

    @Test
    public void shouldProperlyExecuteCommand() throws TelegramApiException {
        //given
        Long chatId = 1234567824356L;

        Update update = new Update();
        Message message = Mockito.mock(Message.class);
        Mockito.when(message.getChatId()).thenReturn(chatId);
        Mockito.when(message.getText()).thenReturn(getCommandName());
        update.setMessage(message);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(getCommandMessage());
        sendMessage.setChatId(chatId.toString());
        sendMessage.enableHtml(true);

        //when
        getCommand().execute(update);

        //then
        Mockito.verify(javaRushTelegramBot).execute(sendMessage);
    }

    public static Update prepareUpdate(Long chatId, String commandName) {
        Update update = new Update();
        Message message = Mockito.mock(Message.class);
        Mockito.when(message.getChatId()).thenReturn(chatId);
        Mockito.when(message.getText()).thenReturn(commandName);
        update.setMessage(message);
        return update;
    }
}
