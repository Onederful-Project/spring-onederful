package com.example.onederful.domain.dashboard.controller;

import com.example.onederful.domain.dashboard.dto.ApiResponse;
import com.example.onederful.domain.dashboard.dto.MyTasksTodayResponseDto;
import com.example.onederful.domain.dashboard.dto.StatisticsResponseDto;
import com.example.onederful.domain.dashboard.service.DashboardService;
import com.example.onederful.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final JwtUtil jwtUtil;

    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<StatisticsResponseDto>> getStatistics(){
        StatisticsResponseDto data = dashboardService.getStatistics();

        return new ResponseEntity<>(
                ApiResponse.success(
                    "통계정보를 조회했습니다.",
                    data
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/my-tasks-today")
    public ResponseEntity<ApiResponse<List<MyTasksTodayResponseDto>>> getMyTasksToday(
            HttpServletRequest request
    ){
        Long userId = jwtUtil.extractId(request);

        List<MyTasksTodayResponseDto> data = dashboardService.getMyTasksToday(userId);

        return new ResponseEntity<>(
                ApiResponse.success(
                        "오늘 내 태스크 정보를 조회했습니다.",
                        data
                ),
                HttpStatus.OK
        );
    }
}
