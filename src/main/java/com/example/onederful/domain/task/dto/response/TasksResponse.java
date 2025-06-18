package com.example.onederful.domain.task.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TasksResponse {

    private List<TaskResponse> content;
    private Long totalElements;
    private Long totalPages;
    private Long size;
    private Long number;
}
