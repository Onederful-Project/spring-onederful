package com.example.onederful.domain.task.service;

import com.example.onederful.domain.task.dto.request.TaskSaveRequest;
import com.example.onederful.domain.task.dto.request.TaskUpdateRequest;
import com.example.onederful.domain.task.dto.response.TaskResponse;
import com.example.onederful.domain.task.entity.Task;
import com.example.onederful.domain.task.enums.ProcessStatus;
import com.example.onederful.domain.task.repository.TaskRepository;
import com.example.onederful.domain.user.entity.User;
import com.example.onederful.domain.user.repository.UserRepository;
import com.example.onederful.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public TaskResponse createTask(TaskSaveRequest request, HttpServletRequest httpServletRequest) {

        Long userId = jwtUtil.extractId(httpServletRequest);

        User me = userRepository.findById(userId).orElseThrow();
        User manager = userRepository.findById(request.getAssigneeId()).orElseThrow();

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

        Task task = taskRepository.findById(id).orElseThrow();

        return TaskResponse.of(task);
    }

    @Transactional(readOnly = true)
    public Page<TaskResponse> findTasks(Pageable pageable, String search, ProcessStatus status) {

        Page<Task> tasks = taskRepository.findTasks(search, status, pageable);

        return tasks.map(TaskResponse::of);
    }

    @Transactional
    public void deleteTask(Long id) {

        Task task = taskRepository.findById(id).orElseThrow();

        task.delete();
    }

    @Transactional
    public TaskResponse updateTask(Long id, TaskUpdateRequest request) {

        Task task = taskRepository.findById(id).orElseThrow();
        User assignee = userRepository.findById(request.getAssigneeId()).orElseThrow();

        if (task.getStatus() == ProcessStatus.DONE) {
            if (request.getStatus() != ProcessStatus.DONE) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "DONE 상태는 이전 상태로 변경 할 수 없습니다.");
            }
        }

        if (task.getStatus() == ProcessStatus.TODO) {
            if (request.getStatus() != ProcessStatus.IN_PROGRESS) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "TODO -> IN_PROCESS으로만 상태 변경이 가능합니다.");
            }
            task.taskStart();
        }

        if (task.getStatus() == ProcessStatus.IN_PROGRESS) {
            if (request.getStatus() != ProcessStatus.DONE) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "IN_PROCESS -> DONE으로만 상태 변경이 가능합니다.");
            }
        }

        task.updateTask(request.getTitle(), request.getDescription(), request.getPriority(),
            assignee,
            request.getDueDate().toLocalDateTime(), request.getStatus());

        return TaskResponse.of(task);
    }
}
