package com.example.onederful.domain.task.controller;

import com.example.onederful.common.ApiResponseDto;
import com.example.onederful.domain.task.common.CreateGroup;
import com.example.onederful.domain.task.common.UpdateGroup;
import com.example.onederful.domain.task.dto.request.TaskSaveRequest;
import com.example.onederful.domain.task.dto.request.TaskStatusUpdateRequest;
import com.example.onederful.domain.task.dto.response.TaskResponse;
import com.example.onederful.domain.task.dto.response.TasksResponse;
import com.example.onederful.domain.task.enums.ProcessStatus;
import com.example.onederful.domain.task.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    public ResponseEntity<ApiResponseDto> createTask(
        @RequestBody @Validated(CreateGroup.class) @Valid TaskSaveRequest request,
        HttpServletRequest httpServletRequest) {

        TaskResponse response = taskService.createTask(request, httpServletRequest);

        return ResponseEntity.ok(ApiResponseDto.success("업무 생성에 성공하였습니다.", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto> findTask(@PathVariable @NotNull Long id) {

        TaskResponse response = taskService.findTask(id);

        return ResponseEntity.ok(ApiResponseDto.success("업무 상세조회에 성공하였습니다.", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto> findTasks(
        @RequestParam(defaultValue = "0") @Min(0) int page,
        @RequestParam(defaultValue = "5") @Min(5) int size,
        @RequestParam(defaultValue = "") String search,
        @RequestParam(defaultValue = "TODO") ProcessStatus status
    ) {
        Pageable pageable = PageRequest.of(page, size, Direction.ASC, "dueDate");

        TasksResponse response = taskService.findTasks(pageable, search, status);

        return ResponseEntity.ok(ApiResponseDto.success("업무 리스트 조회에 성공하였습니다.", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto> deleteTask(@PathVariable @NotNull Long id) {

        taskService.deleteTask(id);

        return ResponseEntity.ok(ApiResponseDto.success("업무 삭제에 성공하였습니다.", null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto> updateTask(@PathVariable @NotNull Long id,
        @RequestBody @Validated(UpdateGroup.class) @Valid TaskSaveRequest request) {

        TaskResponse response = taskService.updateTask(id, request);

        return ResponseEntity.ok(ApiResponseDto.success("업무 수정에 성공하였습니다.", response));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponseDto> updateTaskStatus(@PathVariable @NotNull Long id,
        @RequestBody @Valid
        TaskStatusUpdateRequest request) {

        TaskResponse response = taskService.updateTaskStatus(id, request);

        return ResponseEntity.ok(ApiResponseDto.success("업무 상태 변경에 성공하였습니다.", response));
    }
}
