package com.example.onederful.domain.dashboard.dto;

import com.example.onederful.domain.task.enums.Priority;
import com.example.onederful.domain.task.enums.ProcessStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MyTasksTodayResponseDto {

    private final Long id;

    private final String title;

    private final String contents;

    private final Priority priority;

    private final Long managerId;

    private final Long userId;

    private final LocalDateTime deadline;

    private final ProcessStatus status;

    private final LocalDateTime started_at;

    private final LocalDateTime created_at;

    private final LocalDateTime updated_at;

}
