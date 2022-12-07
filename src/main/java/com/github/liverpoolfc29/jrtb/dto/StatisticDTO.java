package com.github.liverpoolfc29.jrtb.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


/**
 * DTO for getting bot statistics.
 * Этими DTO мы будем пользоваться, чтобы передать данные StatisticService и StatCommand.
 */
@Data
@EqualsAndHashCode
public class StatisticDTO {

    private final int activeUserCount;
    private final int inactiveUserCount;
    private final List<GroupStatDTO> groupStatDTOList;
    private final double averageGroupCountByUser;
}
