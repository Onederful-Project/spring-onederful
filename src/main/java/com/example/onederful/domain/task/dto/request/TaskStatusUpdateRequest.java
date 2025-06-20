package com.example.onederful.domain.task.dto.request;

import com.example.onederful.domain.task.enums.ProcessStatus;
import lombok.Getter;

@Getter
public class TaskStatusUpdateRequest {

    private final ProcessStatus status;

    public TaskStatusUpdateRequest(ProcessStatus status) {
        this.status = status;
    }
}
