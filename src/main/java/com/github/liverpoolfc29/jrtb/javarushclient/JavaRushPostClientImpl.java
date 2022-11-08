package com.github.liverpoolfc29.jrtb.javarushclient;

import com.github.liverpoolfc29.jrtb.javarushclient.dto.PostInfo;
import kong.unirest.GenericType;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JavaRushPostClientImpl implements JavaRushPostClient {

    private final String javaRushApiPostPath;

    public JavaRushPostClientImpl(@Value("${javarush.api.path}") String javaRushApi) {
        this.javaRushApiPostPath = javaRushApi + "/posts";
    }


    @Override
    public List<PostInfo> findNewPosts(Integer groupId, Integer lastPostId) {
        /*
          В запросе добавляем несколько фильтров: (в настройки swagger https://javarush.ru/swagger-ui.html)
          order = NEW — чтобы в списке были вначале новые;
          groupKid = groupId — поиск только по определенным группам;
          limit = 15 — ограничиваем количество статей на запрос. У нас периодичность 15-20 минут и мы ожидаем, что за это время не будет написано БОЛЬШЕ, чем 15(!).
         */
        List<PostInfo> lastPostsByGroup = Unirest.get(javaRushApiPostPath)
                .queryString("order", "NEW")
                .queryString("groupKid", groupId)
                .queryString("limit", 15)
                .asObject(new GenericType<List<PostInfo>>() {
                }).getBody();
        List<PostInfo> newPosts = new ArrayList<>();
        for (PostInfo post : lastPostsByGroup) {
            if (lastPostId.equals(post.getId())) {
                return newPosts;
            }
            newPosts.add(post);
        }
        return newPosts;
    }


}
