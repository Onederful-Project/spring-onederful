package com.example.onederful.domain.log.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.onederful.domain.log.dto.LogResponseDto;
import com.example.onederful.domain.log.dto.addLogTestDto;
import com.example.onederful.domain.log.enums.Activity;
import com.example.onederful.domain.log.service.LogService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LogController {

	private final LogService logService;

	@PostMapping("/api/activities")
	public ResponseEntity<addLogTestDto> addLogTest1() {
		return new ResponseEntity<>(logService.addLogTest1(), HttpStatus.OK);
	}

	@DeleteMapping("/api/activities/{id}")
	public void addLogTest2(@PathVariable Long id) {
		logService.addLogTest2();
	}

	@GetMapping("/api/activities")
	public ResponseEntity<Page<LogResponseDto>> getLog(
		@RequestParam(required = false) Long userId,
		@RequestParam(required = false) String activityStr,
		@RequestParam(required = false) Long targetId,
		@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
		@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end,
		@PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		return new ResponseEntity<>(logService.getLog(userId, activityStr, targetId, start, end, pageable), HttpStatus.OK);
	}
}
