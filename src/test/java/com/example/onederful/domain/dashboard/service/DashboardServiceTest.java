package com.example.onederful.domain.dashboard.service;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.example.onederful.domain.dashboard.dto.MyTasksTodayResponseDto;
import com.example.onederful.domain.dashboard.dto.StatisticsResponseDto;
import com.example.onederful.domain.dashboard.repository.DashboardRepository;
import com.example.onederful.domain.task.entity.Task;
import com.example.onederful.domain.task.enums.Priority;
import com.example.onederful.domain.task.enums.ProcessStatus;
import com.example.onederful.domain.user.entity.User;
import com.example.onederful.domain.user.enums.Role;
import com.example.onederful.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;
import java.time.LocalDateTime;


@ExtendWith(MockitoExtension.class)
public class DashboardServiceTest {
    @InjectMocks
    private DashboardService dashboardService;

    @Mock
    private DashboardRepository dashboardRepository;

    @Mock
    private UserRepository userRepository;

    private User user;
    private Task task;
    @Test
    void 통계_정보_조회_성공(){
        //given
        StatisticsResponseDto statisticsResponseDto =
                 StatisticsResponseDto.builder()
                         .totalTaskCount(5L)
                         .todoTaskCount(2L)
                         .inProgressTaskCount(1L)
                         .doneTaskCount(2L)
                         .taskDoneRate(40.0)
                         .overdueTaskCount(1L)
                 .build();

        given(dashboardRepository.getStatistics()).willReturn(statisticsResponseDto);

        //when
        StatisticsResponseDto actualResult = dashboardService.getStatistics();

        //then
        assertThat(actualResult.getTotalTaskCount()).isEqualTo(5L);
        assertThat(actualResult.getTodoTaskCount()).isEqualTo(2L);
        assertThat(actualResult.getInProgressTaskCount()).isEqualTo(1L);
        assertThat(actualResult.getDoneTaskCount()).isEqualTo(2L);
        assertThat(actualResult.getTaskDoneRate()).isEqualTo(40.0);
    }

    @Test
    void 오늘_내_태스크_조회_성공(){
        //given

        user = User.builder()
                .username("iamgroot")
                .email("iamgroot@example.com")
                .password("Password123!")
                .name("groot")
                .role(Role.USER)
                .build();

        task = Task.builder()
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
                .build();

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(dashboardRepository.getMyTasksToday(1L)).willReturn(List.of(task));

        //when
        List<MyTasksTodayResponseDto> actualResult = dashboardService.getMyTasksToday(1L);

        //then
        assertThat(actualResult).hasSize(1);
        MyTasksTodayResponseDto myTasksTodayResponseDto = actualResult.get(0);
        assertThat(myTasksTodayResponseDto.getId()).isEqualTo(task.getId());
        assertThat(myTasksTodayResponseDto.getContents()).isEqualTo(task.getDescription());
        assertThat(myTasksTodayResponseDto.getManagerId()).isEqualTo(user.getId());

    }
}
