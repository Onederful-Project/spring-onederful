package com.example.onederful.domain.task.dto.request;

import com.example.onederful.domain.task.common.UpdateGroup;
import com.example.onederful.domain.task.enums.Priority;
import com.example.onederful.domain.task.enums.ProcessStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TaskSaveRequest {

    @NotBlank(message = "업무 제목은 필수 항목입니다.")
    @Size(min = 1, max = 100, message = "1자 이상 100자 이하로 입력해주세요.")
    private String title;

    @NotBlank(message = "업무 내용은 필수 항목입니다.")
    private String description;

    @NotNull(message = "업무의 우선순위는 필수 항목입니다.")
    private Priority priority;

    @NotNull(message = "관리자 선택은 필수 항목입니다.")
    private Long assigneeId;

    @NotNull(groups = {UpdateGroup.class}, message = "업무의 상태는 필수 항목입니다.")
    private ProcessStatus status;

    @NotNull(message = "마감일은 필수 항목입니다.")
    @FutureOrPresent(message = "마감일은 오늘이후만 가능합니다.")
    private LocalDateTime dueDate;
}
