package com.example.onederful.domain.log.controller;

import com.example.onederful.common.ApiResponseDto;
import com.example.onederful.common.ListResponse;
import com.example.onederful.domain.log.dto.LogResponse;
import com.example.onederful.domain.log.service.LogService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    /**
     * 활동 로그 조회
     * <p>
     * 검색 조건:
     *
     * @param userId   유저 아이디 (필수 N)
     * @param activity 활동 유형 (필수 N)
     * @param targetId 대상 ID (필수 N)
     * @param start    시작일 (필수 N)
     * @param end      종료일 (필수 N)
     * @param pageable 페이징을 위한 page, size, sort (필수 N)
     * @return 조회된 활동 로그
     */
    @GetMapping("/api/activities")
    public ResponseEntity<ApiResponseDto> getLog(
        @RequestParam(required = false) Long userId,
        @RequestParam(required = false) String activity,
        @RequestParam(required = false) Long targetId,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end,
        @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {

        ListResponse<LogResponse> response = logService.findLog(userId, activity, targetId, start,
            end, pageable);

        return ResponseEntity.ok(ApiResponseDto.success("활동 로그 리스트 조회에 성공하였습니다.", response));
    }
}
