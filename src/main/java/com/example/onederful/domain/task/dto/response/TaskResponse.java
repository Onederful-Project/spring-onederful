package com.example.onederful.domain.task.dto.response;

import com.example.onederful.domain.task.entity.Task;
import com.example.onederful.domain.task.enums.Priority;
import com.example.onederful.domain.task.enums.ProcessStatus;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskResponse {

    private Long id;
    private String title;
    private String description;
    private Priority priority;
    private ProcessStatus status;
    private Long assigneeId;
    private TaskAssignee assignee;

    private OffsetDateTime dueDate;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public static TaskResponse of(Task task) {
        return TaskResponse.builder()
            .id(task.getId())
            .title(task.getTitle())
            .description(task.getDescription())
            .status(task.getStatus())
            .priority(task.getPriority())
            .assigneeId(task.getAssignee().getId())
            .assignee(TaskAssignee.of(task.getAssignee()))
            .dueDate(task.getDueDate().atOffset(ZoneOffset.UTC))
            .createdAt(task.getCreatedAt().atOffset(ZoneOffset.UTC))
            .updatedAt(task.getUpdatedAt().atOffset(ZoneOffset.UTC))
            .build();
    }
}
