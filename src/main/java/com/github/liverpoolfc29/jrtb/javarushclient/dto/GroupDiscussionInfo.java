package com.github.liverpoolfc29.jrtb.javarushclient.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

// data transfer object = TDO - то есть, классы, объекты которых будут носить все нужные для клиента данные.
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class GroupDiscussionInfo extends GroupInfo {

    private UserDiscussionInfo userDiscussionInfo;
    private Integer commentsCount;

}
