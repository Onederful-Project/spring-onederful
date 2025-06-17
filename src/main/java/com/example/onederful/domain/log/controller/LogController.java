package com.example.onederful.domain.log.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.onederful.domain.log.dto.LogResponseDto;
import com.example.onederful.domain.log.service.LogService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LogController {

	private final LogService logService;

	/**
	 * 활동 로그 조회
	 *
	 * 검색 조건:
	 * @param userId 유저 아이디 (필수 N)
	 * @param activity 활동 유형 (필수 N)
	 * @param targetId 대상 ID (필수 N)
	 * @param start 시작일 (필수 N)
	 * @param end 종료일 (필수 N)
	 * @param pageable 페이징을 위한 page, size, sort (필수 N)
	 * @return 조회된 활동 로그
	 */
	@GetMapping("/api/activities")
	public ResponseEntity<Page<LogResponseDto>> getLog(
		@RequestParam(required = false) Long userId,
		@RequestParam(required = false) String activity,
		@RequestParam(required = false) Long targetId,
		@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
		@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end,
		@PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		return new ResponseEntity<>(logService.getLog(userId, activity, targetId, start, end, pageable), HttpStatus.OK);
	}
}
