package com.github.liverpoolfc29.jrtb.service;

import com.github.liverpoolfc29.jrtb.javarushclient.dto.GroupDiscussionInfo;
import com.github.liverpoolfc29.jrtb.repository.GroupSubRepository;
import com.github.liverpoolfc29.jrtb.repository.entity.GroupSub;
import com.github.liverpoolfc29.jrtb.repository.entity.TelegramUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

@DisplayName("Unit-level testing for GroupSubService")
public class GroupSubServiceTest {

    private GroupSubService groupSubService;
    private GroupSubRepository groupSubRepository;
    private TelegramUser newTelegramUser;

    private final static String CHAT_ID = "1";

    @BeforeEach
    public void init() {
        /*
          Добавил здесь еще метод init() с аннотацией BeforeEach. Таким образом обычно создают метод, который будет выполняться перед запуском каждого теста, и в него можно вынести общую логику для всех тестов
          В нашем случае — замокать TelegramUserService нужно одинаково для всех тестов этого класса, поэтому разумно перенести эту логику в общий метод.

          Mockito.when(o1.m1(a1)).thenReturn(o2) — в ней говорим, что когда в объекте o1 будет вызван метод m1 с аргументом a1, метод вернет объект o2.
          Это чуть ли не самая главная функциональность мокито — заставить моковый объект возвращать именно то, что нам нужно.

          Mockito.verify(o1).m1(a1) — который проверяет, что в объекте o1 был вызван метод m1 с аргументом a1. Можно было, конечно, воспользоваться возвращаемым объектом метода save, но я решил сделать немного сложнее,
          показав еще один из возможных способов. Когда он может быть полезен? В случаях когда методы моковых классов возвращают void. Тогда без Mockito.verify дела не будет.
         */
        TelegramUserService telegramUserService = Mockito.mock(TelegramUserService.class);
        groupSubRepository = Mockito.mock(GroupSubRepository.class);
        groupSubService = new GroupSubServiceImpl(groupSubRepository, telegramUserService);

        newTelegramUser = new TelegramUser();
        newTelegramUser.setActive(true);
        newTelegramUser.setChatId(CHAT_ID);

        Mockito.when(telegramUserService.findByChatId(CHAT_ID)).thenReturn(Optional.of(newTelegramUser));
    }

    @Test
    public void shouldProperlySaveGroup() {
        //given
        GroupDiscussionInfo groupDiscussionInfo = new GroupDiscussionInfo();
        groupDiscussionInfo.setId(1);
        groupDiscussionInfo.setTitle("g1");

        GroupSub expectedGroupSub = new GroupSub();
        expectedGroupSub.setId(groupDiscussionInfo.getId());
        expectedGroupSub.setTitle(groupDiscussionInfo.getTitle());
        expectedGroupSub.addUser(newTelegramUser);

        //when
        groupSubService.save(CHAT_ID, groupDiscussionInfo);

        //then
        Mockito.verify(groupSubRepository).save(expectedGroupSub);
    }

    @Test
    public void shouldProperlyAddUserToExistingGroup() {
        //given
        TelegramUser oldTelegramUser = new TelegramUser();
        oldTelegramUser.setChatId("2");
        oldTelegramUser.setActive(true);

        GroupDiscussionInfo groupDiscussionInfo = new GroupDiscussionInfo();
        groupDiscussionInfo.setId(1);
        groupDiscussionInfo.setTitle("g1");

        GroupSub groupFromDb = new GroupSub();
        groupFromDb.setId(groupDiscussionInfo.getId());
        groupFromDb.setTitle(groupDiscussionInfo.getTitle());
        groupFromDb.addUser(oldTelegramUser);

        Mockito.when(groupSubRepository.findById(groupDiscussionInfo.getId())).thenReturn(Optional.of(groupFromDb));

        GroupSub expectedGroupSub = new GroupSub();
        expectedGroupSub.setId(groupDiscussionInfo.getId());
        expectedGroupSub.setTitle(groupDiscussionInfo.getTitle());
        expectedGroupSub.addUser(oldTelegramUser);
        expectedGroupSub.addUser(newTelegramUser);

        //when
        groupSubService.save(CHAT_ID, groupDiscussionInfo);

        //then
        Mockito.verify(groupSubRepository).findById(groupDiscussionInfo.getId());
        Mockito.verify(groupSubRepository).save(expectedGroupSub);
    }

}
