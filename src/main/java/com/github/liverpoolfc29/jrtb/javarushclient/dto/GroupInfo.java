package com.github.liverpoolfc29.jrtb.javarushclient.dto;

import lombok.Data;
import lombok.ToString;

/**
 * Group Info DTO class.
 */
@Data
@ToString
public class GroupInfo {

    private String avatarUrl;
    private String createTime;
    private String description;
    private Integer id;
    private String key;
    private Integer levelToEditor;
    private MeGroupInfo meGroupInfo;
    private String pictureUrl;
    private String title;
    private GroupInfoType type;
    private Integer userCount;
    private GroupVisibilityStatus visibilityStatus;
}
