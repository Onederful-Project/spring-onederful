package com.example.onederful.domain.task.service;

import com.example.onederful.domain.task.dto.request.TaskSaveRequest;
import com.example.onederful.domain.task.dto.request.TaskStatusUpdateRequest;
import com.example.onederful.domain.task.dto.response.TaskResponse;
import com.example.onederful.domain.task.dto.response.TasksResponse;
import com.example.onederful.domain.task.entity.Task;
import com.example.onederful.domain.task.enums.ProcessStatus;
import com.example.onederful.domain.task.repository.TaskRepository;
import com.example.onederful.domain.user.entity.User;
import com.example.onederful.domain.user.repository.UserRepository;
import com.example.onederful.exception.CustomException;
import com.example.onederful.exception.ErrorCode;
import com.example.onederful.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public TaskResponse createTask(TaskSaveRequest request, HttpServletRequest httpServletRequest) {

        Long userId = jwtUtil.extractId(httpServletRequest);

        User me = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.NONEXISTENT_USER));
        User manager = userRepository.findById(request.getAssigneeId())
            .orElseThrow(() -> new CustomException(ErrorCode.NONEXISTENT_USER));

        Task task = Task.builder()
            .title(request.getTitle())
            .description(request.getDescription())
            .priority(request.getPriority())
            .assignee(manager)
            .user(me)
            .status(ProcessStatus.TODO)
            .dueDate(request.getDueDate().toLocalDateTime())
            .build();

        Task savedTask = taskRepository.save(task);

        return TaskResponse.of(savedTask);
    }

    @Transactional(readOnly = true)
    public TaskResponse findTask(Long id) {

        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.NONEXISTENT_TASK));

        return TaskResponse.of(task);
    }

    @Transactional(readOnly = true)
    public TasksResponse findTasks(Pageable pageable, String search, ProcessStatus status) {

        Page<Task> tasks = taskRepository.findTasks(search, status, pageable);

        return TasksResponse.builder()
            .content(tasks.getContent().stream().map(TaskResponse::of).collect(Collectors.toList()))
            .totalElements(tasks.getTotalElements())
            .size((long) tasks.getSize())
            .number((long) tasks.getNumber())
            .totalPages((long) tasks.getTotalPages())
            .build();
    }

    @Transactional
    public void deleteTask(Long id) {

        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.NONEXISTENT_TASK));

        task.delete();
    }

    @Transactional
    public TaskResponse updateTaskStatus(Long id, TaskStatusUpdateRequest request) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.NONEXISTENT_TASK));

        taskUpdateValid(task, request.getStatus());

        task.updateTaskStatus(request.getStatus());

        return TaskResponse.of(task);
    }

    @Transactional
    public TaskResponse updateTask(Long id, TaskSaveRequest request) {

        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.NONEXISTENT_TASK));
        User assignee = userRepository.findById(request.getAssigneeId())
            .orElseThrow(() -> new CustomException(ErrorCode.NONEXISTENT_USER));

        taskUpdateValid(task, request.getStatus());

        task.updateTask(request.getTitle(), request.getDescription(), request.getPriority(),
            assignee,
            request.getDueDate().toLocalDateTime(), request.getStatus());

        return TaskResponse.of(task);
    }

    @Transactional(readOnly = true)
    public Task findById(Long id) {
        return taskRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.NONEXISTENT_TASK));
    }

    private void taskUpdateValid(Task task, ProcessStatus status) {
        if (task.getStatus() == ProcessStatus.DONE) {
            if (status != ProcessStatus.DONE) {
                throw new CustomException(ErrorCode.BAD_REQUEST_STATUS);
            }
        }

        if (task.getStatus() == ProcessStatus.TODO) {
            if (status != ProcessStatus.IN_PROGRESS) {
                throw new CustomException(ErrorCode.BAD_REQUEST_STATUS);
            }
            task.taskStart();
        }

        if (task.getStatus() == ProcessStatus.IN_PROGRESS) {
            if (status != ProcessStatus.DONE) {
                throw new CustomException(ErrorCode.BAD_REQUEST_STATUS);
            }
        }
    }
}
