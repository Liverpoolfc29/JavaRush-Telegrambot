package com.github.liverpoolfc29.jrtb.service;

import com.github.liverpoolfc29.jrtb.javarushclient.JavaRushPostClient;
import com.github.liverpoolfc29.jrtb.javarushclient.dto.PostInfo;
import com.github.liverpoolfc29.jrtb.repository.entity.GroupSub;
import com.github.liverpoolfc29.jrtb.repository.entity.TelegramUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FindNewPostsServiceImpl implements FindNewPostsService {

    public static final String JAVARUSH_WEB_POST_FORMAT = "https://javarush.ru/groups/posts/%s";

    private final GroupSubService groupSubService;
    private final JavaRushPostClient javaRushPostClient;
    private final SendBotMessageService sendBotMessageService;

    @Autowired
    public FindNewPostsServiceImpl(GroupSubService groupSubService,
                                   JavaRushPostClient javaRushPostClient,
                                   SendBotMessageService sendBotMessageService) {
        this.groupSubService = groupSubService;
        this.javaRushPostClient = javaRushPostClient;
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void findNewPosts() {
        // При помощи groupService мы находим все группы, которые есть в БД.
        // Потом разбегаемся по всем группам и для каждой вызываем — javaRushPostClient.findNewPosts.
        groupSubService.findAll().forEach(groupSub -> {
            List<PostInfo> newPosts = javaRushPostClient.findNewPosts(groupSub.getId(), groupSub.getLastPostId());

            setNewLastPostId(groupSub, newPosts);

            notifySubscribersAboutNewPosts(groupSub, newPosts);
        });
    }

    private void notifySubscribersAboutNewPosts(GroupSub groupSub, List<PostInfo> newPosts) {
        Collections.reverse(newPosts);
        List<String> messagesWithNewPosts = newPosts.stream()
                .map(post -> String.format("Вышла новая статья <b>%s</b> в группе <b>%s</b>.✨\n\n" +
                                "<b>Описание:</b> %s\n\n" +
                                "<b>Ссылка:</b> %s\n",
                        post.getTitle(), groupSub.getTitle(), post.getDescription(), getPostUrl(post.getKey())))
                .collect(Collectors.toList());

        // при помощи того, что у GroupSub есть коллекция пользователей, пробегаем по активным и отсылаем уведомления о новых статьях.
        groupSub.getTelegramUsers().stream()
                .filter(TelegramUser::isActive)
                .forEach(it -> sendBotMessageService.sendMessage(it.getChatId(), messagesWithNewPosts));
    }

    private void setNewLastPostId(GroupSub groupSub, List<PostInfo> newPosts) {
        // Далее при помощи метода setNewPostId мы обновляем ID нашей последней новой статьи, чтобы наша база данных знала, что мы уже обработали новые.
        newPosts.stream().mapToInt(PostInfo::getId).max()
                .ifPresent(id -> {
                    groupSub.setLastPostId(id);
                    groupSubService.save(groupSub);
                });
    }

    private Object getPostUrl(String key) {
        return String.format(JAVARUSH_WEB_POST_FORMAT, key);
    }

}