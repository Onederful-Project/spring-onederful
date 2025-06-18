package com.example.onederful.domain.dashboard.repository;

import com.example.onederful.domain.dashboard.dto.MyTasksTodayResponseDto;
import com.example.onederful.domain.dashboard.dto.StatisticsResponseDto;
import com.example.onederful.domain.task.entity.Task;

import java.util.List;

public interface DashboardRepository {
    StatisticsResponseDto getStatistics();
    List<Task> getMyTasksToday(Long userId);
}
