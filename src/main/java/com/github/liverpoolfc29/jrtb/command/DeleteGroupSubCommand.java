package com.github.liverpoolfc29.jrtb.command;

import com.github.liverpoolfc29.jrtb.repository.entity.GroupSub;
import com.github.liverpoolfc29.jrtb.repository.entity.TelegramUser;
import com.github.liverpoolfc29.jrtb.service.GroupSubService;
import com.github.liverpoolfc29.jrtb.service.SendBotMessageService;
import com.github.liverpoolfc29.jrtb.service.TelegramUserService;
import org.springframework.util.CollectionUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.liverpoolfc29.jrtb.command.CommandName.DELETE_GROUP_SUB;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.isNumeric;

/**
 * Delete Group subscription {@link Command}.
 */
public class DeleteGroupSubCommand implements Command {

    private final SendBotMessageService sendBotMessageService;
    private final TelegramUserService telegramUserService;
    private final GroupSubService groupSubService;

    public DeleteGroupSubCommand(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService, GroupSubService groupSubService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
        this.groupSubService = groupSubService;
    }


    @Override
    public void execute(Update update) {
        if (update.getMessage().getText().equalsIgnoreCase(DELETE_GROUP_SUB.getCommandName())) {
            sendGroupIdList(update.getMessage().getChatId());
            return;
        }
        String groupId = update.getMessage().getText().split(SPACE)[1];
        Long chatId = update.getMessage().getChatId();

        if (isNumeric(groupId)) {
            Optional<GroupSub> optionalGroupSub = groupSubService.findById(Integer.valueOf(groupId));
            if (optionalGroupSub.isPresent()) {
                GroupSub groupSub = optionalGroupSub.get();
                TelegramUser telegramUser = telegramUserService.findByChatId(chatId).orElseThrow(NotFoundException::new);
                groupSub.getTelegramUsers().remove(telegramUser);
                groupSubService.save(groupSub);
                sendBotMessageService.sendMessage(chatId, format("Удалил подписку на группу: %s", groupSub.getTitle()));
            } else {
                sendBotMessageService.sendMessage(chatId, "Не нашел такой группы =/");
            }
        } else {
            sendBotMessageService.sendMessage(chatId, "Неправильный формат ID группы.\n " +
                    "ID должно быть целым положительным числом");
        }
    }

    private void sendGroupIdList(Long chatId) {
        String message;
        List<GroupSub> groupSubList = telegramUserService.findByChatId(chatId)
                .orElseThrow(NotFoundException::new)
                .getGroupSubs();
        if (CollectionUtils.isEmpty(groupSubList)) {
            message = "Пока нет подписок на группы. Чтобы добавить подписку напишите /addGroupSub";
        } else {
            String userGroupData = groupSubList.stream()
                    .map(group -> format("%s - %s \n", group.getTitle(), group.getId()))
                    .collect(Collectors.joining());

            message = String.format("Чтобы удалить подписку на группу - передайте команду вместе с ID группы. \n" +
                    "Например: /deleteGroupSub 16 \n\n" +
                    "я подготовил список всех групп, на которые Вы подписаны \n\n" +
                    "имя группы - ID группы \n\n" +
                    "%s", userGroupData);
        }

        sendBotMessageService.sendMessage(chatId, message);
    }

}