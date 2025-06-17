package com.example.onederful.domain.task.service;

import com.example.onederful.domain.task.dto.request.TaskSaveRequest;
import com.example.onederful.domain.task.dto.response.TaskResponse;
import com.example.onederful.domain.task.entity.Task;
import com.example.onederful.domain.task.enums.ProcessStatus;
import com.example.onederful.domain.task.repository.TaskRepository;
import com.example.onederful.domain.task.repository.UserRepository;
import com.example.onederful.domain.user.entity.User;
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

    // 유저 레포지토리 임의로 task domain 내부 작성
    private final UserRepository userRepository;

    @Transactional
    public void createTask(TaskSaveRequest request) {

        User manager = userRepository.findById(request.getManagerId()).orElseThrow();

        // user (생성한 사람은 추후 로그인 연동 후 수정 예정)
        Task task = Task.builder()
            .title(request.getTitle())
            .content(request.getContent())
            .priority(request.getPriority())
            .status(request.getStatus())
            .manager(manager)
            .user(manager)
            .status(ProcessStatus.TODO)
            .dueAt(request.getDueAt().atStartOfDay())
            .build();

        taskRepository.save(task);
    }

    @Transactional(readOnly = true)
    public TaskResponse findTask(Long id) {

        Task task = taskRepository.findById(id).orElseThrow();

        return TaskResponse.of(task);
    }

    @Transactional(readOnly = true)
    public Page<TaskResponse> findTasks(Pageable pageable, String keyword, ProcessStatus status) {

        Page<Task> tasks = taskRepository.findTasks(keyword, status, pageable);

        return tasks.map(TaskResponse::of);
    }

    @Transactional
    public void deleteTask(Long id) {

        // 권한 검사는 로그인 병합 이후

        Task task = taskRepository.findById(id).orElseThrow();
        task.delete();
    }

    @Transactional
    public void updateTask(Long id, TaskSaveRequest request) {

        // 권한 검사는 로그인 병합 이후

        Task task = taskRepository.findById(id).orElseThrow();
        User manager = userRepository.findById(request.getManagerId()).orElseThrow();

        if (task.getStatus() == ProcessStatus.DONE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "DONE 상태는 이전 상태로 변경 할 수 없습니다.");
        }

        if (task.getStatus() == ProcessStatus.TODO) {
            if (request.getStatus() != ProcessStatus.IN_PROGRESS) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "TODO -> IN_PROCESS으로만 상태 변경이 가능합니다.");
            }
        }

        if (task.getStatus() == ProcessStatus.IN_PROGRESS) {
            if (request.getStatus() != ProcessStatus.DONE) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "IN_PROCESS -> DONE으로만 상태 변경이 가능합니다.");
            }
        }

        task.updateTask(request.getTitle(), request.getContent(), request.getPriority(), manager,
            request.getDueAt().atStartOfDay(), request.getStatus());
    }
}
