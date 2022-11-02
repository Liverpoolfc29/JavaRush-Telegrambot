package com.github.liverpoolfc29.jrtb.command;

import com.github.liverpoolfc29.jrtb.repository.entity.GroupSub;
import com.github.liverpoolfc29.jrtb.repository.entity.TelegramUser;
import com.github.liverpoolfc29.jrtb.service.GroupSubService;
import com.github.liverpoolfc29.jrtb.service.SendBotMessageService;
import com.github.liverpoolfc29.jrtb.service.TelegramUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static com.github.liverpoolfc29.jrtb.command.CommandName.DELETE_GROUP_SUB;
import static com.github.liverpoolfc29.jrtb.command.AbstractCommandTest.prepareUpdate;

@DisplayName("Unit-level testing for DeleteGroupSubCommand")
public class DeleteGroupSubCommandTest {

    private Command command;
    private SendBotMessageService sendBotMessageService;
    private GroupSubService groupSubService;
    private TelegramUserService telegramUserService;

    @BeforeEach
    public void init() {
        sendBotMessageService = Mockito.mock(SendBotMessageService.class);
        groupSubService = Mockito.mock(GroupSubService.class);
        telegramUserService = Mockito.mock(TelegramUserService.class);

        command = new DeleteGroupSubCommand(sendBotMessageService, telegramUserService, groupSubService);
    }

    @Test
    public void shouldProperlyReturnEmptySubscriptionList() {
        /// просто передали команду /deleteGroupSub и нет подписок на группы
        //given
        Long chatId = 23456L;
        Update update = prepareUpdate(chatId, DELETE_GROUP_SUB.getCommandName());

        Mockito.when(telegramUserService.findByChatId(String.valueOf(chatId)))
                .thenReturn(Optional.of(new TelegramUser()));

        String expectedMessage = "Пока нет подписок на группы. Чтобы добавить подписку напиши /addGroupSub";

        //when
        command.execute(update);

        //then
        Mockito.verify(sendBotMessageService).sendMessage(chatId.toString(), expectedMessage);
    }

    @Test
    public void shouldProperlyReturnSubscriptionList() {
        /// просто передали команду /deleteGroupSub и есть подписки на группы
        //given
        Long chatId = 23456L;
        Update update = prepareUpdate(chatId, DELETE_GROUP_SUB.getCommandName());
        TelegramUser telegramUser = new TelegramUser();
        GroupSub gs1 = setNewGroupSub(123, "GS1 Title");
        //gs1.setId(123);
        //gs1.setTitle("GS1 Title");
        telegramUser.setGroupSubs(Collections.singletonList(gs1));
        Mockito.when(telegramUserService.findByChatId(String.valueOf(chatId)))
                .thenReturn(Optional.of(telegramUser));

        String expectedMessage = "Чтобы удалить подписку на группу - передайте команду вместе с ID группы. \n" +
                "Например: /deleteGroupSub 16 \n\n" +
                "я подготовил список всех групп, на которые вы подписаны \n\n" +
                "имя группы - ID группы \n\n" +
                "GS1 Title - 123 \n";

        //when
        command.execute(update);

        //then
        Mockito.verify(sendBotMessageService).sendMessage(chatId.toString(), expectedMessage);
    }

    @Test
    public void shouldRejectByInvalidGroupId() {
        /// передали невалидный ID группы, например, /deleteGroupSub abc
        //given
        Long chatId = 23456L;
        Update update = prepareUpdate(chatId, String.format("%s %s", DELETE_GROUP_SUB.getCommandName(), "groupSubId"));
        TelegramUser telegramUser = new TelegramUser();
        GroupSub groupSub1 = setNewGroupSub(1234, "GS1 Title");
        //groupSub1.setId(123);
        //groupSub1.setTitle("GS1 Title");
        telegramUser.setGroupSubs(Collections.singletonList(groupSub1));
        Mockito.when(telegramUserService.findByChatId(String.valueOf(chatId)))
                .thenReturn(Optional.of(telegramUser));

        String expectedMessage = "неправильный формат ID группы. \n " +
                "ID должно быть целым положительным числом";

        //when
        command.execute(update);

        //then
        Mockito.verify(sendBotMessageService).sendMessage(chatId.toString(), expectedMessage);
    }

    @Test
    public void shouldProperlyDeleteByGroupId() {
        /// все правильно удалится, как и ожидается
        //given
        /// prepare update object
        Long chatId = 2345L;
        Integer groupId = 1234;
        Update update = prepareUpdate(chatId, String.format("%s %s", DELETE_GROUP_SUB.getCommandName(), groupId));

        GroupSub groupSub1 = setNewGroupSub(123, "SG1 Title");
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setChatId(chatId.toString());
        telegramUser.setGroupSubs(Collections.singletonList(groupSub1));
        ArrayList<TelegramUser> userArrayList = new ArrayList<>();
        userArrayList.add(telegramUser);
        groupSub1.setTelegramUsers(userArrayList);
        Mockito.when(groupSubService.findById(groupId)).thenReturn(Optional.of(groupSub1));
        Mockito.when(telegramUserService.findByChatId(chatId.toString()))
                .thenReturn(Optional.of(telegramUser));

        String expectedMessage = "Удалил подписку на группу: GS1 Title";

        //when
        command.execute(update);

        //then
        userArrayList.remove(telegramUser);
        Mockito.verify(groupSubService).save(groupSub1);
        Mockito.verify(sendBotMessageService).sendMessage(chatId.toString(), expectedMessage);
    }

    @Test
    public void shouldDoesNotExistByGroupId() {
        /// когда ID группы валидный, но такой группы нет в БД
        //given
        Long chatId = 2345L;
        Integer groupId = 1234;

        Update update = prepareUpdate(chatId, String.format("%s %s", DELETE_GROUP_SUB.getCommandName(), groupId));

        Mockito.when(groupSubService.findById(groupId)).thenReturn(Optional.empty());

        String expectedMessage = "Не нашел такой группы =/";

        //when
        command.execute(update);

        //then
        Mockito.verify(groupSubService).findById(groupId);
        Mockito.verify(sendBotMessageService).sendMessage(chatId.toString(), expectedMessage);
    }

    private GroupSub setNewGroupSub(Integer id, String title) {
        GroupSub groupSub = new GroupSub();
        groupSub.setId(id);
        groupSub.setTitle(title);
        return groupSub;
    }

}