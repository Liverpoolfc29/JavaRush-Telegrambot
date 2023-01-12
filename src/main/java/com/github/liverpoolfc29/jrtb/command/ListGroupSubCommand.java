package com.github.liverpoolfc29.jrtb.command;

import com.github.liverpoolfc29.jrtb.repository.entity.GroupSub;
import com.github.liverpoolfc29.jrtb.repository.entity.TelegramUser;
import com.github.liverpoolfc29.jrtb.service.SendBotMessageService;
import com.github.liverpoolfc29.jrtb.service.TelegramUserService;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.ws.rs.NotFoundException;
import java.util.stream.Collectors;

/**
 * {@link Command} for getting list of {@link GroupSub}.
 * Здесь все максимально просто — по имеющемуся chat_id получаем пользователя, и у него уже в объекте будут собраны все его подписки на группы
 */
public class ListGroupSubCommand implements Command {

    private final SendBotMessageService sendBotMessageService;
    private final TelegramUserService telegramUserService;

    public ListGroupSubCommand(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public void execute(Update update) {
        //todo add exception handling
        TelegramUser telegramUser = telegramUserService.findByChatId(update.getMessage().getChatId())
                .orElseThrow(NotFoundException::new);

        String message;
        if (telegramUser.getGroupSubs().isEmpty()) {
            message = "Пока нет подписок на группы. Что бы добавить подписку напишите /addGroupSub, или вызовите помощь командой /help";
        } else {
            String collectedGroups = telegramUser.getGroupSubs().stream()
                    .map(group -> String.format("Group %s, ID = %s\n", group.getTitle(), group.getId()))
                    .collect(Collectors.joining());
            message = String.format("Я нашел все подписки на группы: \n\n%s", collectedGroups);
        }
        sendBotMessageService.sendMessage(telegramUser.getChatId(), message);
    }

}