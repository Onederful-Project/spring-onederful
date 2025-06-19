package com.example.onederful.domain.task.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.example.onederful.domain.task.dto.request.TaskSaveRequest;
import com.example.onederful.domain.task.dto.request.TaskStatusUpdateRequest;
import com.example.onederful.domain.task.entity.Task;
import com.example.onederful.domain.task.enums.Priority;
import com.example.onederful.domain.task.enums.ProcessStatus;
import com.example.onederful.domain.task.repository.TaskRepository;
import com.example.onederful.domain.user.entity.User;
import com.example.onederful.domain.user.enums.Role;
import com.example.onederful.domain.user.repository.UserRepository;
import com.example.onederful.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private TaskService taskService;

    @Test
    @DisplayName("업무 생성이 성공한다.")
    void 업무_생성_성공_테스트() {
        //given
        TaskSaveRequest request = TaskSaveRequest.builder()
            .title("title")
            .description("description")
            .priority(Priority.LOW)
            .assigneeId(1L)
            .dueDate(LocalDateTime.of(2027, 4, 2, 23, 59, 59))
            .build();

        HttpServletRequest httpServletRequest = new MockHttpServletRequest();

        Long userId = 1L;

        User me = User.builder()
            .id(userId)
            .email("me@example.com")
            .name("me1")
            .password("!@A12345")
            .role(Role.USER)
            .username("me1")
            .build();

        User manager = User.builder()
            .id(userId)
            .email("manager@example.com")
            .name("manager")
            .password("!@A12345")
            .role(Role.USER)
            .username("manager")
            .build();

        Task task = Task.builder()
            .id(2L)
            .title(request.getTitle())
            .description(request.getDescription())
            .priority(request.getPriority())
            .assignee(manager)
            .user(me)
            .status(ProcessStatus.TODO)
            .dueDate(request.getDueDate())
            .build();

        ReflectionTestUtils.setField(task, "createdAt", LocalDateTime.now());
        ReflectionTestUtils.setField(task, "updatedAt", LocalDateTime.now());

        given(jwtUtil.extractId(any(HttpServletRequest.class))).willReturn(userId);
        given(userRepository.findById(me.getId())).willReturn(Optional.of(me));
        given(userRepository.findById(manager.getId())).willReturn(Optional.of(manager));
        given(taskRepository.save(any(Task.class))).willReturn(task);

        //when

        taskService.createTask(request, httpServletRequest);

        //then
        Assertions.assertEquals(task.getTitle(), request.getTitle());
        Assertions.assertEquals(task.getDescription(), request.getDescription());

        verify(taskRepository).save(any(Task.class));
    }

    @Test
    @DisplayName("업무 세부사항 조회가 성공한다.")
    void 업무_조회_성공_테스트() {
        //given
        Long userId = 1L;

        User me = User.builder()
            .id(userId)
            .email("me@example.com")
            .name("me1")
            .password("!@A12345")
            .role(Role.USER)
            .username("me1")
            .build();

        User manager = User.builder()
            .id(userId)
            .email("manager@example.com")
            .name("manager")
            .password("!@A12345")
            .role(Role.USER)
            .username("manager")
            .build();

        Task task = Task.builder()
            .id(2L)
            .title("test")
            .description("description")
            .priority(Priority.LOW)
            .assignee(manager)
            .user(me)
            .status(ProcessStatus.TODO)
            .dueDate(LocalDateTime.now())
            .build();

        ReflectionTestUtils.setField(task, "createdAt", LocalDateTime.now());
        ReflectionTestUtils.setField(task, "updatedAt", LocalDateTime.now());

        given(taskRepository.findById(anyLong())).willReturn(Optional.of(task));

        //when

        taskService.findTask(anyLong());

        //then

        Assertions.assertEquals("test", task.getTitle());
        Assertions.assertEquals("description", task.getDescription());

        verify(taskRepository).findById(anyLong());
    }

    @Test
    @DisplayName("업무 세부사항 조회가 성공한다.")
    void 업무_리스트_조회_성공_테스트() {
        //given
        Long userId = 1L;

        User me = User.builder()
            .id(userId)
            .email("me@example.com")
            .name("me1")
            .password("!@A12345")
            .role(Role.USER)
            .username("me1")
            .build();

        User manager = User.builder()
            .id(userId)
            .email("manager@example.com")
            .name("manager")
            .password("!@A12345")
            .role(Role.USER)
            .username("manager")
            .build();

        Task task1 = Task.builder()
            .id(2L)
            .title("test1")
            .description("description1")
            .priority(Priority.LOW)
            .assignee(manager)
            .user(me)
            .status(ProcessStatus.TODO)
            .dueDate(LocalDateTime.now())
            .build();

        Task task2 = Task.builder()
            .id(2L)
            .title("test2")
            .description("description2")
            .priority(Priority.LOW)
            .assignee(manager)
            .user(me)
            .status(ProcessStatus.TODO)
            .dueDate(LocalDateTime.now())
            .build();

        ReflectionTestUtils.setField(task1, "createdAt", LocalDateTime.now());
        ReflectionTestUtils.setField(task1, "updatedAt", LocalDateTime.now());
        ReflectionTestUtils.setField(task2, "createdAt", LocalDateTime.now());
        ReflectionTestUtils.setField(task2, "updatedAt", LocalDateTime.now());

        List<Task> list = List.of(task1, task2);
        Page<Task> tasks = new PageImpl<>(list);

        Pageable pageable = PageRequest.of(0, 10);
        String search = "test";
        ProcessStatus status = ProcessStatus.TODO;

        given(taskRepository.findTasks(search, status, pageable)).willReturn(tasks);

        //when

        taskService.findTasks(pageable, search, status);

        //then
        Assertions.assertEquals(2, tasks.getTotalElements());
        Assertions.assertEquals(1, tasks.getTotalPages());

        verify(taskRepository).findTasks(search, status, pageable);
    }

    @Test
    @DisplayName("업무 삭제가 성공한다.")
    void 업무_삭제_성공_테스트() {
        //given
        Long userId = 1L;

        User me = User.builder()
            .id(userId)
            .email("me@example.com")
            .name("me1")
            .password("!@A12345")
            .role(Role.USER)
            .username("me1")
            .build();

        User manager = User.builder()
            .id(userId)
            .email("manager@example.com")
            .name("manager")
            .password("!@A12345")
            .role(Role.USER)
            .username("manager")
            .build();

        Task task = Task.builder()
            .id(2L)
            .title("test")
            .description("description")
            .priority(Priority.LOW)
            .assignee(manager)
            .user(me)
            .status(ProcessStatus.TODO)
            .dueDate(LocalDateTime.now())
            .build();

        ReflectionTestUtils.setField(task, "createdAt", LocalDateTime.now());
        ReflectionTestUtils.setField(task, "updatedAt", LocalDateTime.now());

        given(taskRepository.findById(anyLong())).willReturn(Optional.of(task));

        //when

        taskService.deleteTask(task.getId());

        //then

        Assertions.assertTrue(task.getIsDeleted());
    }

    @Test
    @DisplayName("업무 수정이 성공한다.")
    void 업무_수정_성공_테스트() {
        //given

        Long id = 1L;

        TaskSaveRequest request = TaskSaveRequest.builder()
            .title("title")
            .description("description")
            .priority(Priority.LOW)
            .assigneeId(1L)
            .status(ProcessStatus.IN_PROGRESS)
            .dueDate(LocalDateTime.of(2027, 4, 2, 23, 59, 59))
            .build();

        Long userId = 1L;

        User me = User.builder()
            .id(userId)
            .email("me@example.com")
            .name("me1")
            .password("!@A12345")
            .role(Role.USER)
            .username("me1")
            .build();

        User manager = User.builder()
            .id(request.getAssigneeId())
            .email("manager@example.com")
            .name("manager")
            .password("!@A12345")
            .role(Role.USER)
            .username("manager")
            .build();

        Task task = Task.builder()
            .id(2L)
            .title("test")
            .description("description")
            .priority(Priority.LOW)
            .assignee(me)
            .user(me)
            .status(ProcessStatus.TODO)
            .dueDate(LocalDateTime.now())
            .build();

        ReflectionTestUtils.setField(task, "createdAt", LocalDateTime.now());
        ReflectionTestUtils.setField(task, "updatedAt", LocalDateTime.now());

        given(taskRepository.findById(anyLong())).willReturn(Optional.of(task));
        given(userRepository.findById(anyLong())).willReturn(Optional.of(manager));

        //when

        taskService.updateTask(id, request);

        //then

        Assertions.assertEquals(task.getAssignee(), manager);
        Assertions.assertEquals(ProcessStatus.IN_PROGRESS, task.getStatus());
    }

    @Test
    @DisplayName("업무 상태 변경이 성공한다.")
    void 업무_상태_변경_성공_테스트() {
        //given

        Long id = 1L;

        TaskStatusUpdateRequest request = new TaskStatusUpdateRequest(ProcessStatus.IN_PROGRESS);

        Long userId = 1L;

        User me = User.builder()
            .id(userId)
            .email("me@example.com")
            .name("me1")
            .password("!@A12345")
            .role(Role.USER)
            .username("me1")
            .build();

        Task task = Task.builder()
            .id(2L)
            .title("test")
            .description("description")
            .priority(Priority.LOW)
            .assignee(me)
            .user(me)
            .status(ProcessStatus.TODO)
            .dueDate(LocalDateTime.now())
            .build();

        ReflectionTestUtils.setField(task, "createdAt", LocalDateTime.now());
        ReflectionTestUtils.setField(task, "updatedAt", LocalDateTime.now());

        given(taskRepository.findById(anyLong())).willReturn(Optional.of(task));

        //when

        taskService.updateTaskStatus(id, request);

        //then

        Assertions.assertEquals(ProcessStatus.IN_PROGRESS, task.getStatus());
    }

    @Test
    @DisplayName("기본 조회가 성공한다.")
    void 기본_조회_성공_테스트() {
        Long id = 1L;

        Long userId = 1L;

        User me = User.builder()
            .id(userId)
            .email("me@example.com")
            .name("me1")
            .password("!@A12345")
            .role(Role.USER)
            .username("me1")
            .build();

        Task task = Task.builder()
            .id(2L)
            .title("test")
            .description("description")
            .priority(Priority.LOW)
            .assignee(me)
            .user(me)
            .status(ProcessStatus.TODO)
            .dueDate(LocalDateTime.now())
            .build();

        ReflectionTestUtils.setField(task, "createdAt", LocalDateTime.now());
        ReflectionTestUtils.setField(task, "updatedAt", LocalDateTime.now());

        given(taskRepository.findById(anyLong())).willReturn(Optional.of(task));

        taskService.findById(id);

        verify(taskRepository).findById(anyLong());
    }
}
