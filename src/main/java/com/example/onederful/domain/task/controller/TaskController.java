package com.example.onederful.domain.task.controller;

import com.example.onederful.domain.task.dto.request.TaskSaveRequest;
import com.example.onederful.domain.task.dto.request.TaskUpdateRequest;
import com.example.onederful.domain.task.dto.response.CommonResponse;
import com.example.onederful.domain.task.dto.response.TaskResponse;
import com.example.onederful.domain.task.enums.ProcessStatus;
import com.example.onederful.domain.task.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<CommonResponse<TaskResponse>> createTask(
        @RequestBody @Valid TaskSaveRequest request, HttpServletRequest httpServletRequest) {

        TaskResponse response = taskService.createTask(request, httpServletRequest);

        return ResponseEntity.ok(
            CommonResponse.create(true, "업무 생성 성공", response,
                OffsetDateTime.now()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<TaskResponse>> findTask(@PathVariable @NotNull Long id) {

        TaskResponse response = taskService.findTask(id);

        return ResponseEntity.ok(
            CommonResponse.create(true, "업무 상세조회 성공", response,
                OffsetDateTime.now()));
    }

    @GetMapping
    public ResponseEntity<CommonResponse<Page<TaskResponse>>> findTasks(
        @RequestParam(defaultValue = "1") @Min(1) int page,
        @RequestParam(defaultValue = "5") @Min(5) int size,
        @RequestParam(defaultValue = "") String keyword,
        @RequestParam(defaultValue = "TODO") ProcessStatus status
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Direction.ASC, "dueDate");

        Page<TaskResponse> response = taskService.findTasks(pageable, keyword,
            status);

        return ResponseEntity.ok(
            CommonResponse.create(true, "업무 리스트 조회 성공", response,
                OffsetDateTime.now()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<Void>> deleteTask(@PathVariable @NotNull Long id) {

        taskService.deleteTask(id);

        return ResponseEntity.ok(
            CommonResponse.create(true, "업무 삭제 성공", null,
                OffsetDateTime.now()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<TaskResponse>> updateTask(@PathVariable @NotNull Long id,
        @RequestBody @Valid TaskUpdateRequest request) {

        TaskResponse response = taskService.updateTask(id, request);

        return ResponseEntity.ok(
            CommonResponse.create(true, "업무 수정 성공", response, OffsetDateTime.now()));
    }
}
