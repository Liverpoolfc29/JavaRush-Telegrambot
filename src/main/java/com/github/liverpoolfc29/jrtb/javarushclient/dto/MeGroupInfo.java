package com.github.liverpoolfc29.jrtb.javarushclient.dto;

import lombok.Data;

/**
 * Group information related to authorized user. If there is no user - will be null.
 */
@Data
public class MeGroupInfo {
    private MeGroupInfoStatus meGroupInfoStatus;
    private Integer userGroupId;
}
