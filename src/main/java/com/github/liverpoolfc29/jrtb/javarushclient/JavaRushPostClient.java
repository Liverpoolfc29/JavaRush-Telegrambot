package com.github.liverpoolfc29.jrtb.javarushclient;

import com.github.liverpoolfc29.jrtb.javarushclient.dto.PostInfo;

import java.util.List;

/**
 * Client for Javarush Open API corresponds to Posts.
 */
public interface JavaRushPostClient {

    /**
     * Find new posts since lastPostId in provided group.
     *
     * @param groupId    provided group ID.
     * @param lastPostId provided last post ID.
     * @return the collection of the new {@link PostInfo}.
     */

    List<PostInfo> findNewPosts(Integer groupId, Integer lastPostId);
    // Принимает два аргумента: ID группы и последний ID статьи, которую бот уже отправлял. Поэтому передадутся все те статьи, которые вышли позже статьи с lastPostId.
}
