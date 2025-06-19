package com.example.onederful.domain.log.service;

import java.time.LocalDate;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.onederful.domain.comment.dto.CreateCommentResponseDataDto;
import com.example.onederful.domain.log.dto.LogResponse;
import com.example.onederful.domain.log.dto.LogsResponse;
import com.example.onederful.domain.log.entity.Log;
import com.example.onederful.domain.log.enums.Activity;
import com.example.onederful.domain.log.enums.Method;
import com.example.onederful.domain.log.repository.LogRepository;
import com.example.onederful.domain.log.repository.LogSpecification;
import com.example.onederful.domain.task.dto.response.TaskResponse;
import com.example.onederful.domain.user.dto.Tokeninfo;
import com.example.onederful.domain.user.entity.User;
import com.example.onederful.domain.user.repository.UserRepository;
import com.example.onederful.exception.CustomException;
import com.example.onederful.exception.ErrorCode;
import com.example.onederful.security.JwtUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogService {

	private final LogRepository logRepository;
	private final UserRepository userRepositry;
	private final JwtUtil jwtUtil;

	// log 조회 메서드
	public LogsResponse findLog(
		Long userId, String activityStr, Long targetId,
		LocalDate start, LocalDate end, Pageable pageable) {

		// 활동 유형 Enum 형태로 변환
		Activity activity = null;
		try {
			if (activityStr != null) {
				activity = Activity.valueOf(activityStr);
			}
		} catch (IllegalArgumentException e) {
			throw new CustomException(ErrorCode.INVALID_ACTIVITY);
		}

		// 들어온 조건 여부로 동적 쿼리 설정
		Specification<Log> spec =
			LogSpecification.hasUserId(userId)
			.and(LogSpecification.hasActivity(activity))
			.and(LogSpecification.hasTargetId(targetId))
			.and(LogSpecification.betweenDates(start, end));

		Page<Log> logs = logRepository.findAll(spec, pageable);

		return LogsResponse.builder()
			.content(logs.getContent().stream().map(LogResponse::of).collect(Collectors.toList()))
			.totalElements(logs.getTotalElements())
			.size((long) logs.getSize())
			.number((long) logs.getNumber())
			.totalPages((long) logs.getTotalPages())
			.build();
	}

	// 로그인 시 로그 기록
	@Transactional
	public void saveLoginLog(String ip, Method method, String url, Object result) {
		// userId
		Long userId = null;
		if (result instanceof Tokeninfo) {
			String token = ((Tokeninfo) result).getToken();
			userId = jwtUtil.extractAllClaims(token).get("id", Long.class);
		}

		// 현재 유저 조회
		User user  = userRepositry.findById(userId).orElseThrow(
			() -> new CustomException(ErrorCode.UNAUTHORIZED)
		);

		// 활동 유형
		Activity activity = Activity.USER_LOGGED_IN;

		// 대상 id
		Long targetId = userId;

		// 로그 DB에 저장
		Log log = Log.builder()
			.user(user)
			.activity(activity)
			.ipAddress(ip)
			.method(method)
			.targetId(targetId)
			.requestUrl(url)
			.build();

		logRepository.save(log);
	}

	// 생성, 수정, 삭제 시 로그 기록
	@Transactional
	public void saveCudLog(String ip, Method method, String url, Long userId, Object result) {
		// 현재 유저 조회
		User user  = userRepositry.findById(userId).orElseThrow(
			() -> new CustomException(ErrorCode.UNAUTHORIZED)
		);

		// 활동 유형 -> 요청 메서드와 url로 일치하는 활동 유형 찾기
		Activity activity = null;
		if (method.equals(Method.POST) && url.contains("/comments")) {
			activity = Activity.COMMENT_CREATED;
		}
		else if (method.equals(Method.PUT) && url.contains("/comments")) {
			activity = Activity.COMMENT_UPDATED;
		}
		else if (method.equals(Method.DELETE) && url.contains("/comments")) {
			activity = Activity.COMMENT_DELETED;
		}
		else if (method.equals(Method.POST) && url.contains("/tasks")) {
			activity = Activity.TASK_CREATED;
		}
		else if (method.equals(Method.PUT) && url.contains("/tasks")) {
			activity = Activity.TASK_UPDATED;
		}
		else if (method.equals(Method.DELETE) && url.contains("/tasks")) {
			activity = Activity.TASK_DELETED;
		}


		// 대상 id -> 생성인 경우 응답에서 / 수정과 삭제의 경우 url 마지막에서 찾기
		Long targetId = null;
		if (activity.equals(Activity.TASK_CREATED)) {
			if (result instanceof TaskResponse) {
				targetId = ((TaskResponse) result).getId();
			}
		}
		else if (activity.equals(Activity.COMMENT_CREATED)) {
			if (result instanceof CreateCommentResponseDataDto) {
				targetId = ((CreateCommentResponseDataDto) result).getId();
			}
		}
		else {
			String[] parts = url.split("/");
			String lastPart = parts[parts.length - 1];  // /api/.../{id}의 id
			targetId =  Long.parseLong(lastPart);
		}

		// 로그 DB에 저장
		Log log = Log.builder()
			.user(user)
			.activity(activity)
			.ipAddress(ip)
			.method(method)
			.targetId(targetId)
			.requestUrl(url)
			.build();

		logRepository.save(log);
	}

	// 상태 변경 시 로그 기록
	public void saveTaskStatusChangeLog(String ip, Method method, String url, Long userId, Long targetId, Activity activity) {
		// 현재 유저 조회
		User user  = userRepositry.findById(userId).orElseThrow(
			() -> new CustomException(ErrorCode.UNAUTHORIZED)
		);

		// 로그 DB에 저장
		Log log = Log.builder()
			.user(user)
			.activity(activity)
			.ipAddress(ip)
			.method(method)
			.targetId(targetId)
			.requestUrl(url)
			.build();

		logRepository.save(log);
	}
}
