package com.github.liverpoolfc29.jrtb.service;

import com.github.liverpoolfc29.jrtb.javarushclient.JavaRushGroupClient;
import com.github.liverpoolfc29.jrtb.javarushclient.JavaRushPostClient;
import com.github.liverpoolfc29.jrtb.javarushclient.dto.GroupDiscussionInfo;
import com.github.liverpoolfc29.jrtb.repository.GroupSubRepository;
import com.github.liverpoolfc29.jrtb.repository.entity.GroupSub;
import com.github.liverpoolfc29.jrtb.repository.entity.TelegramUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

/**
 * Чтобы Spring Data заработала правильно и создалась запись many-to-many, нам нужно для группы подписки, которую мы создаем, достать из нашей БД юзера
 * и добавить его в объект GroupSub. Тем самым, когда мы передадим на сохранение эту подписку, будет создана еще и связь через таблицу group_x_user.
 * Может быть ситуация, когда уже создана такая группа подписки и нужно просто добавить к ней еще одного юзера.
 * Для этого мы сперва получаем по ID группы из БД, и если запись есть, работаем с ней, если нет — создаем новую.
 * Важно отметить, что для работы с TelegramUser мы используем TelegramUserService, чтобы следовать последнему из принципов SOLID.
 */
@Service
public class GroupSubServiceImpl implements GroupSubService {

    private final GroupSubRepository groupSubRepository;
    private final TelegramUserService telegramUserService;
    private final JavaRushGroupClient javaRushGroupClient;

    @Autowired
    public GroupSubServiceImpl(GroupSubRepository groupSubRepository, TelegramUserService telegramUserService, JavaRushGroupClient javaRushGroupClient) {
        this.groupSubRepository = groupSubRepository;
        this.telegramUserService = telegramUserService;
        this.javaRushGroupClient = javaRushGroupClient;
    }


    @Override
    public GroupSub save(Long chatId, GroupDiscussionInfo groupDiscussionInfo) {
        TelegramUser telegramUser = telegramUserService.findByChatId(chatId).orElseThrow(NotFoundException::new);
        //TODO add exception handling
        GroupSub groupSub;
        Optional<GroupSub> groupSubFromDb = groupSubRepository.findById(groupDiscussionInfo.getId());
        if (groupSubFromDb.isPresent()) {
            groupSub = groupSubFromDb.get();
            Optional<TelegramUser> first = groupSub.getTelegramUsers().stream()
                    .filter(it -> it.getChatId().equals(chatId))
                    .findFirst();
            if (first.isEmpty()) {
                groupSub.addUser(telegramUser);
            }
        } else {
            groupSub = new GroupSub();
            groupSub.addUser(telegramUser);
            groupSub.setLastPostId(javaRushGroupClient.findLastPostId(groupDiscussionInfo.getId()));
            groupSub.setId(groupDiscussionInfo.getId());
            groupSub.setTitle(groupDiscussionInfo.getTitle());
        }
        return groupSubRepository.save(groupSub);
    }

    @Override
    public GroupSub save(GroupSub groupSub) {
        return groupSubRepository.save(groupSub);
    }

    @Override
    public Optional<GroupSub> findById(Integer id) {
        return groupSubRepository.findById(id);
    }

    @Override
    public List<GroupSub> findAll() {
        return groupSubRepository.findAll();
    }

}
