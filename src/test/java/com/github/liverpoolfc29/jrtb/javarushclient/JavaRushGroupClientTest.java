package com.github.liverpoolfc29.jrtb.javarushclient;

import com.github.liverpoolfc29.jrtb.javarushclient.dto.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.liverpoolfc29.jrtb.javarushclient.dto.GroupInfoType.TECH;

@DisplayName("Integration-level testing for JavaRushGroupClientImplTest")
public class JavaRushGroupClientTest {

    public static final String JAVARUSH_API_PATH = "https://javarush.ru/api/1.0/rest";

    private final JavaRushGroupClient groupClient = new JavaRushGroupClientImpl(JAVARUSH_API_PATH);

    @Test
    public void shouldProperlyGetGroupsWithEmptyArgs() {
        //given
        GroupRequestArgs args = GroupRequestArgs.builder().build();

        //when
        List<GroupInfo> groupInfoList = groupClient.getGroupList(args);

        //then
        Assertions.assertNotNull(groupInfoList);
        Assertions.assertFalse(groupInfoList.isEmpty());
    }

    @Test
    public void shouldProperlyGetWithOffSetAndLimit() {
        //given
        GroupRequestArgs args = GroupRequestArgs.builder()
                .offset(1)
                .limit(3)
                .build();

        //when
        List<GroupInfo> groupInfoList = groupClient.getGroupList(args);

        //then
        Assertions.assertNotNull(groupInfoList);
        Assertions.assertEquals(3, groupInfoList.size());
    }

    @Test
    public void shouldProperlyGetGroupsDiscWithEmptyArgs() {
        //given
        GroupRequestArgs args = GroupRequestArgs.builder().build();

        //when
        List<GroupDiscussionInfo> groupDiscussionInfoList = groupClient.getGroupDiscussionList(args);

        //then
        Assertions.assertNotNull(groupDiscussionInfoList);
        Assertions.assertFalse(groupDiscussionInfoList.isEmpty());
    }

    @Test
    public void shouldProperlyGetGroupDiscWithOffSetAndLimit() {
        //given
        GroupRequestArgs args = GroupRequestArgs.builder()
                .offset(1)
                .limit(3)
                .build();

        //when
        List<GroupDiscussionInfo> groupDiscussionInfoList = groupClient.getGroupDiscussionList(args);

        //then
        Assertions.assertNotNull(groupDiscussionInfoList);
        Assertions.assertEquals(3, groupDiscussionInfoList.size());
    }

    @Test
    public void shouldProperlyGetGroupCount() {
        //given
        GroupRequestArgs args = GroupRequestArgs.builder().build();

        //when
        Integer groupCount = groupClient.getGroupCount(args);

        //then
        Assertions.assertEquals(32, groupCount);
    }

    @Test
    public void shouldProperlyGetGroupTECHCount() {
        //given
        GroupRequestArgs args = GroupRequestArgs.builder()
                .type(TECH)
                .build();

        //when
        Integer groupCount = groupClient.getGroupCount(args);

        //then
        Assertions.assertEquals(7, groupCount);
    }

    @Test
    public void shouldProperlyGetGroupById() {
        //given
        Integer freelancerGroup = 19;

        //when
        GroupDiscussionInfo groupById = groupClient.getGroupById(freelancerGroup);

        //then
        Assertions.assertNotNull(groupById);
        Assertions.assertEquals(19, groupById.getId());
        Assertions.assertEquals(TECH, groupById.getType());
        Assertions.assertEquals("freelancer", groupById.getKey());
        Assertions.assertEquals("Freelancer", groupById.getTitle());
    }

    @Test
    public void shouldProperlyGetGroupByIdV2() {
        //given
        Integer javaDeveloper = 18;

        //when
        GroupDiscussionInfo groupById = groupClient.getGroupById(javaDeveloper);

        //then
        Assertions.assertNotNull(groupById);
        Assertions.assertEquals(18, groupById.getId());
        Assertions.assertEquals(TECH, groupById.getType());
        Assertions.assertEquals("java-developer", groupById.getKey());
        Assertions.assertEquals("Java Developer", groupById.getTitle());
    }

}
