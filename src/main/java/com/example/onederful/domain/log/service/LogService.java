package com.example.onederful.domain.log.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.onederful.domain.log.dto.LogResponseDto;
import com.example.onederful.domain.log.dto.addLogTestDto;
import com.example.onederful.domain.log.entity.Log;
import com.example.onederful.domain.log.enums.Activity;
import com.example.onederful.domain.log.enums.Method;
import com.example.onederful.domain.log.repository.LogRepository;
import com.example.onederful.domain.log.repository.LogSpecification;
import com.example.onederful.domain.user.entity.User;
import com.example.onederful.domain.user.enums.Role;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogService {

	private final LogRepository logRepository;
	private final UserRepository userRepositry;

	public addLogTestDto addLogTest1() {
		return new addLogTestDto(1L);
	}

	public void addLogTest2() {
	}

	public Page<LogResponseDto> getLog(
		Long userId,
		String activityStr,
		Long targetId,
		LocalDate start,
		LocalDate end,
		Pageable pageable) {

		// 들어온 조건 여부로 동적 쿼리 설정
		Specification<Log> spec =
			LogSpecification.hasUserId(userId)
			.and(LogSpecification.hasActivity(activityStr))
			.and(LogSpecification.hasTargetId(targetId))
			.and(LogSpecification.betweenDates(start, end));

		return logRepository.findAll(spec, pageable)
			.map(LogResponseDto::of);
	}

	@Transactional
	public void saveLog(String ip, Method method, String url, Object result) {
		// TODO: 로그인한 정보를 토대로 User 가져오도록 구현
		// 로직 구현용 더미유저 하드 코딩 내용
		User dummyUser = User.builder()
			.email("dummy@example.com")
			.password("password")
			.name("Dummy User")
			.role(Role.USER)
			.isDeleted(false)
			.build();
		userRepositry.save(dummyUser);
		//User dummyUser = userRepositry.findById(1L).get();

		// TODO: 어떤 활동인지, 대상 id 찾기
		// 활동 유형 -> 요청 메서드와 url로 일치하는 활동 유형 찾기
		Activity activity = null;
		if (method.equals(Method.POST) && url.contains("/activities")) {
			activity = Activity.TEST;
		}
		else if (method.equals(Method.DELETE) && url.contains("/activities")) {
			activity = Activity.TEST;
		}

		// 대상 id -> 생성인 경우 응답에서 / 수정과 삭제의 경우 url 마지막에서 찾기
		Long targetId = null;
		if (method.equals(Method.POST)) {
			if (result instanceof addLogTestDto) {
				targetId = ((addLogTestDto) result).getId();
			}
		}
		else if (method.equals(Method.PATCH) || method.equals(Method.DELETE)) {
			String[] parts = url.split("/");
			String lastPart = parts[parts.length - 1];  // /api/.../{id}의 id
			targetId =  Long.parseLong(lastPart);
		}

		// if (activity == null || targetId == null) 예외 처리

		Log log = Log.builder()
			.user(dummyUser)
			.activity(activity)
			.ipAddress(ip)
			.method(method)
			.targetId(targetId)
			.requestUrl(url)
			.build();

		logRepository.save(log);
	}
}
