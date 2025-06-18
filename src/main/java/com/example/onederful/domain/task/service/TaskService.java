package com.example.onederful.domain.task.service;

import com.example.onederful.domain.task.dto.request.TaskSaveRequest;
import com.example.onederful.domain.task.dto.response.TaskResponse;
import com.example.onederful.domain.task.entity.Task;
import com.example.onederful.domain.task.enums.ProcessStatus;
import com.example.onederful.domain.task.repository.TaskRepository;
import com.example.onederful.domain.user.entity.User;
import com.example.onederful.domain.user.repository.UserRepository;
import com.example.onederful.exception.CustomException;
import com.example.onederful.exception.ErrorCode;
import com.example.onederful.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
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
    public Page<TaskResponse> findTasks(Pageable pageable, String search, ProcessStatus status) {

        Page<Task> tasks = taskRepository.findTasks(search, status, pageable);

        return tasks.map(TaskResponse::of);
    }

    @Transactional
    public void deleteTask(Long id) {

        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.NONEXISTENT_TASK));

        task.delete();
    }

    @Transactional
    public TaskResponse updateTask(Long id, TaskSaveRequest request) {

        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.NONEXISTENT_TASK));
        User assignee = userRepository.findById(request.getAssigneeId())
            .orElseThrow(() -> new CustomException(ErrorCode.NONEXISTENT_USER));

        if (task.getStatus() == ProcessStatus.DONE) {
            if (request.getStatus() != ProcessStatus.DONE) {
                throw new CustomException(ErrorCode.BAD_REQUEST_STATUS);
            }
        }

        if (task.getStatus() == ProcessStatus.TODO) {
            if (request.getStatus() != ProcessStatus.IN_PROGRESS) {
                throw new CustomException(ErrorCode.BAD_REQUEST_STATUS);
            }
            task.taskStart();
        }

        if (task.getStatus() == ProcessStatus.IN_PROGRESS) {
            if (request.getStatus() != ProcessStatus.DONE) {
                throw new CustomException(ErrorCode.BAD_REQUEST_STATUS);
            }
        }

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
}
