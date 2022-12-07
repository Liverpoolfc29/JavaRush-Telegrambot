package com.github.liverpoolfc29.jrtb.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DTO for showing group id and title without data
 * Этими DTO мы будем пользоваться, чтобы передать данные StatisticService и StatCommand.
 */

@Data
@EqualsAndHashCode(exclude = {"title", "activeUserCount"})
public class GroupStatDTO {

    private final Integer id;
    private final String title;
    private final Integer activeUserCount;

}
