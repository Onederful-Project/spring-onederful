package com.example.onederful.domain.task.dto.response;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "create")
public class CommonResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private OffsetDateTime timestamp;
}
