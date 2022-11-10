package com.github.liverpoolfc29.jrtb.javarushclient;

import com.github.liverpoolfc29.jrtb.javarushclient.dto.GroupDiscussionInfo;
import com.github.liverpoolfc29.jrtb.javarushclient.dto.GroupInfo;
import com.github.liverpoolfc29.jrtb.javarushclient.dto.GroupRequestArgs;
//import com.github.liverpoolfc29.jrtb.javarushclient.dto.GroupsCountRequestArgs;

import java.util.List;

/**
 * Client for Javarush Open API corresponds to Groups.
 */

public interface JavaRushGroupClient {
    /**
     * Get all the {@link GroupInfo} filtered by provided {@link GroupRequestArgs}.
     *
     * @param requestArgs provided {@link GroupRequestArgs}.
     * @return the collection of the {@link GroupInfo} objects.
     */
    List<GroupInfo> getGroupList(GroupRequestArgs requestArgs);

    /**
     * Get all the {@link GroupDiscussionInfo} filtered by provided {@link GroupRequestArgs}.
     *
     * @param requestArgs provided {@link GroupRequestArgs}
     * @return the collection of the {@link GroupDiscussionInfo} objects.
     */
    List<GroupDiscussionInfo> getGroupDiscussionList(GroupRequestArgs requestArgs);

    /**
     * Get count of groups filtered by provided {@link GroupRequestArgs}.
     *
     * @param countRequestArgs provided {@link GroupRequestArgs}.
     * @return the count of the groups.
     */
    Integer getGroupCount(GroupRequestArgs countRequestArgs);

    /**
     * Get {@link GroupDiscussionInfo} by provided ID.
     *
     * @param id provided ID.
     * @return {@link GroupDiscussionInfo} object.
     */
    GroupDiscussionInfo getGroupById(Integer id);


    Integer findLastPostId (Integer groupSubId);

}
