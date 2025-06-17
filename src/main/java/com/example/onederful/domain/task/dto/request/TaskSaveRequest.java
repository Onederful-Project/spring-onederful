package com.example.onederful.domain.task.dto.request;

import com.example.onederful.domain.task.enums.Priority;
import com.example.onederful.domain.task.enums.ProcessStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskSaveRequest {

    @NotBlank(message = "업무 제목은 필수 항목입니다.")
    private String title;

    @NotBlank(message = "업무 내용은 필수 항목입니다.")
    private String description;

    @NotNull(message = "업무의 우선순위는 필수 항목입니다.")
    private Priority priority;

    @NotNull(message = "관리자 선택은 필수 항목입니다.")
    private Long managerId;

    @NotNull(message = "업무의 상태는 필수 항목입니다.")
    private ProcessStatus status;

    @NotNull(message = "마감일은 필수 항목입니다.")
    @FutureOrPresent(message = "마감일은 오늘이후만 가능합니다.")
    private OffsetDateTime dueDate;
}
