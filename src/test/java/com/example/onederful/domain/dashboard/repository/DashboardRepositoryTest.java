package com.example.onederful.domain.dashboard.repository;

import com.example.onederful.domain.dashboard.dto.StatisticsResponseDto;
import com.example.onederful.domain.task.entity.Task;
import com.example.onederful.domain.task.enums.Priority;
import com.example.onederful.domain.task.enums.ProcessStatus;
import com.example.onederful.domain.task.repository.TaskRepository;
import com.example.onederful.domain.user.entity.User;
import com.example.onederful.domain.user.enums.Role;
import com.example.onederful.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;


import java.time.LocalDateTime;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
public class DashboardRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    private DashboardRepositoryImpl dashboardRepository;

    private User user;

    @Test
    void 통계_정보_조회_성공(){
        //given
        dashboardRepository = new DashboardRepositoryImpl(em);

        user = User.builder()
                .username("iamgroot")
                .email("iamgroot@example.com")
                .password("Password123!")
                .name("groot")
                .role(Role.USER)
                .build();

        userRepository.save(user);

        taskRepository.save(Task.builder()
                .title("Task1")
                .description("Task1 Content")
                .priority(Priority.HIGH)
                .assignee(user)
                .user(user)
                .status(ProcessStatus.TODO)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .startedAt(LocalDateTime.now().plusDays(1))
                .dueDate(LocalDateTime.now().plusDays(5))
                .build()
        );

        taskRepository.save(Task.builder()
                .title("Task2")
                .description("Task2 Content")
                .priority(Priority.MEDIUM)
                .assignee(user)
                .user(user)
                .status(ProcessStatus.IN_PROGRESS)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .startedAt(LocalDateTime.now())
                .dueDate(LocalDateTime.now().minusDays(5))
                .build()
        );

        taskRepository.save(Task.builder()
                .title("Task3")
                .description("Task3 Content")
                .priority(Priority.HIGH)
                .assignee(user)
                .user(user)
                .status(ProcessStatus.DONE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .startedAt(LocalDateTime.now().minusDays(1))
                .dueDate(LocalDateTime.now().plusDays(5))
                .build()
        );

        taskRepository.save(Task.builder()
                .title("Task4")
                .description("Task4 Content")
                .priority(Priority.LOW)
                .assignee(user)
                .user(user)
                .status(ProcessStatus.TODO)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .startedAt(LocalDateTime.now().plusDays(1))
                .dueDate(LocalDateTime.now().plusDays(5))
                .build()
        );

        taskRepository.save(Task.builder()
                .title("Task5")
                .description("Task5 Content")
                .priority(Priority.LOW)
                .assignee(user)
                .user(user)
                .status(ProcessStatus.DONE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .startedAt(LocalDateTime.now().minusDays(2))
                .dueDate(LocalDateTime.now().plusDays(5))
                .build()
        );
        em.flush();
        em.clear();

        //when
        StatisticsResponseDto result = dashboardRepository.getStatistics();

        //then
        assertThat(result.getTotalTaskCount()).isEqualTo(5);
        assertThat(result.getTodoTaskCount()).isEqualTo(2);
        assertThat(result.getInProgressTaskCount()).isEqualTo(1);
        assertThat(result.getDoneTaskCount()).isEqualTo(2);
        assertThat(result.getOverdueTaskCount()).isEqualTo(1);
        assertThat(result.getTaskDoneRate()).isEqualTo(40.0);
    }

    @Test
    void 오늘_내_태스크_조회(){
        //given
        dashboardRepository = new DashboardRepositoryImpl(em);

        user = User.builder()
                .username("iamgroot")
                .email("iamgroot@example.com")
                .password("Password123!")
                .name("groot")
                .role(Role.USER)
                .build();

        userRepository.save(user);

        taskRepository.save(Task.builder()
                .title("Task1")
                .description("Task1 Content")
                .priority(Priority.LOW)
                .assignee(user)
                .user(user)
                .status(ProcessStatus.TODO)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .startedAt(LocalDateTime.now())
                .dueDate(LocalDateTime.now().plusDays(5))
                .build()
        );

        taskRepository.save(Task.builder()
                .title("Task2")
                .description("Task2 Content")
                .priority(Priority.HIGH)
                .assignee(user)
                .user(user)
                .status(ProcessStatus.TODO)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .startedAt(LocalDateTime.now())
                .dueDate(LocalDateTime.now().plusDays(5))
                .build()
        );


        taskRepository.save(Task.builder()
                .title("Task3")
                .description("Task3 Content")
                .priority(Priority.MEDIUM)
                .assignee(user)
                .user(user)
                .status(ProcessStatus.IN_PROGRESS)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .startedAt(LocalDateTime.now())
                .dueDate(LocalDateTime.now().minusDays(5))
                .build()
        );

        taskRepository.save(Task.builder()
                .title("Task4")
                .description("Task4 Content")
                .priority(Priority.HIGH)
                .assignee(user)
                .user(user)
                .status(ProcessStatus.DONE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .startedAt(LocalDateTime.now().minusDays(1))
                .dueDate(LocalDateTime.now().plusDays(5))
                .build()
        );

        taskRepository.save(Task.builder()
                .title("Task5")
                .description("Task5 Content")
                .priority(Priority.LOW)
                .assignee(user)
                .user(user)
                .status(ProcessStatus.DONE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .startedAt(LocalDateTime.now().minusDays(2))
                .dueDate(LocalDateTime.now().plusDays(5))
                .build()
        );

        //when
        List<Task> taskList = dashboardRepository.getMyTasksToday(user.getId());

        //then
        assertThat(taskList).hasSize(3);
        assertThat(taskList.get(0).getPriority()).isEqualTo(Priority.HIGH);
        assertThat(taskList.get(1).getPriority()).isEqualTo(Priority.MEDIUM);
        assertThat(taskList.get(2).getPriority()).isEqualTo(Priority.LOW);

    }

}
