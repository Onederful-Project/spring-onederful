package com.example.onederful.domain.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.example.onederful.domain.log.service.LogService;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

	private final HttpRequestUtil httpRequestUtil;
	private final LogService logService;

	@Pointcut(
		"execution(* com.example..TaskService.createTask(..)) || " +
		"execution(* com.example..TaskService.updateTask(..)) || " +
		"execution(* com.example..TaskService.deleteTask(..))"
	)
	public void cudMethods() {}

	@Pointcut("execution(* com.example..userService.login())")
	public void loginMethods() {}

	// 생성, 수정, 삭제
	@AfterReturning(pointcut = "cudMethods()", returning = "result")
	public void logCudMethods(JoinPoint joinPoint, Object result) {
		System.out.println("메서드 정상 실행 후: 로그 기록");

		// HttpServletRequest으로부터 요청 ip, 메서드, url
		HttpRequestUtil.RequestInfo request = httpRequestUtil.getRequestInfo();

		// 로그 저장
		logService.saveLog(request.getIp(), request.getMethod(), request.getUrl(), request.getUserId(), result);
	}

	// 상태 변경
	// @Around("serviceMethods()")
	// public void logTaskStatusChange() {
	// 	// 변경 전 task 상태 조회
	//  taskService.findStatus();
	//
	// 	// 메서드 실행
	//
	// 	// 변경 후 tast 상태 조회 및 다른 정보들 뽑기
	//	taskService.findStatus();
 	//
	// 	// 로그 저장
	//	logService.saveLog();
	// }

	// 로그인/로그아웃

}
