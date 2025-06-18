package com.example.onederful.domain.dashboard.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StatisticsResponseDto {

    private final Long totalTaskCount;

    private final Long todoTaskCount;

    private final Long inProgressTaskCount;

    private final Long doneTaskCount;

    private final Double taskDoneRate;

    private final Long overdueTaskCount;

}
