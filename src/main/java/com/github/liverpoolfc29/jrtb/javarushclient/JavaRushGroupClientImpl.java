package com.github.liverpoolfc29.jrtb.javarushclient;

import com.github.liverpoolfc29.jrtb.javarushclient.dto.GroupDiscussionInfo;
import com.github.liverpoolfc29.jrtb.javarushclient.dto.GroupInfo;
import com.github.liverpoolfc29.jrtb.javarushclient.dto.GroupRequestArgs;
//import com.github.liverpoolfc29.jrtb.javarushclient.dto.GroupsCountRequestArgs;
import com.github.liverpoolfc29.jrtb.javarushclient.dto.PostInfo;
import kong.unirest.GenericType;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static org.aspectj.util.LangUtil.isEmpty;

/**
 * Implementation of the {@link JavaRushGroupClient} interface.
 */

@Component
public class JavaRushGroupClientImpl implements JavaRushGroupClient {

    private final String javarushApiGroupPath;
    private final String getJavarushApiPostPath;

    //  Value подразумевает, что значение внутри аннотации соответствует полю в файле с пропертями. Поэтому нужно в application.properties добавить строку
    public JavaRushGroupClientImpl(@Value("${javarush.api.path}") String javarushApi) {
        this.javarushApiGroupPath = javarushApi + "/groups";
        this.getJavarushApiPostPath = javarushApi + "/posts";
    }

    @Override
    public List<GroupInfo> getGroupList(GroupRequestArgs requestArgs) {
        return Unirest.get(javarushApiGroupPath)
                .queryString(requestArgs.populateQueries())
                .asObject(new GenericType<List<GroupInfo>>() {
                }).getBody();
    }

    @Override
    public List<GroupDiscussionInfo> getGroupDiscussionList(GroupRequestArgs requestArgs) {
        return Unirest.get(javarushApiGroupPath)
                .queryString(requestArgs.populateQueries())
                .asObject(new GenericType<List<GroupDiscussionInfo>>() {
                }).getBody();
    }

    @Override
    public Integer getGroupCount(GroupRequestArgs countRequestArgs) {
        return Integer.valueOf(
                Unirest.get(String.format("%s/count", javarushApiGroupPath))
                        .queryString(countRequestArgs.populateQueries())
                        .asString()
                        .getBody()
        );
    }

    @Override
    public GroupDiscussionInfo getGroupById(Integer id) {
        return Unirest.get(String.format("%s/group%s", javarushApiGroupPath, id.toString()))
                .asObject(GroupDiscussionInfo.class)
                .getBody();
    }

    @Override
    public Integer findLastPostId(Integer groupSubId) {
        List<PostInfo> posts = Unirest.get(getJavarushApiPostPath)
                .queryString("order", "NEW")
                .queryString("groupKid", groupSubId.toString())
                .queryString("limit", "1")
                .asObject(new GenericType<List<PostInfo>>() {
                })
                .getBody();

        return isEmpty(posts) ? 0 : Optional.ofNullable(posts.get(0)).map(PostInfo::getId).orElse(0);
    }

}
