package com.example.onederful.domain.dashboard.service;

import com.example.onederful.domain.dashboard.dto.MyTasksTodayResponseDto;
import com.example.onederful.domain.dashboard.dto.StatisticsResponseDto;
import com.example.onederful.domain.dashboard.repository.DashboardRepository;
import com.example.onederful.domain.task.entity.Task;
import com.example.onederful.domain.user.repository.UserRepository;
import com.example.onederful.exception.CustomException;
import com.example.onederful.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardRepository dashboardRepository;
    private final UserRepository userRepository;

    public StatisticsResponseDto getStatistics() {

        return dashboardRepository.getStatistics();
    }

    public List<MyTasksTodayResponseDto> getMyTasksToday(Long userId) {

        userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.NONEXISTENT_USER));

        List<Task> tasks = dashboardRepository.getMyTasksToday(userId);
        List<MyTasksTodayResponseDto> dtos = tasks.stream()
            .map(t -> MyTasksTodayResponseDto.builder()
                .id(t.getId())
                .title(t.getTitle())
                .contents(t.getDescription())
                .priority(t.getPriority())
                .managerId(t.getAssignee().getId())
                .userId(t.getUser().getId())
                .deadline(t.getDueDate())
                .status(t.getStatus())
                .started_at(t.getStartedAt())
                .created_at(t.getCreatedAt())
                .updated_at(t.getUpdatedAt())
                .build()
            )
            .toList();

        return dtos;
    }
}
