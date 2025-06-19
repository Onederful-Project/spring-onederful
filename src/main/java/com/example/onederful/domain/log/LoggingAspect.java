package com.example.onederful.domain.log;

import com.example.onederful.domain.log.enums.Activity;
import com.example.onederful.domain.log.service.LogService;
import com.example.onederful.domain.task.entity.Task;
import com.example.onederful.domain.task.enums.ProcessStatus;
import com.example.onederful.domain.task.service.TaskService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

    private final HttpRequestUtil httpRequestUtil;
    private final TaskService taskService;
    private final LogService logService;

    @Pointcut("execution(* com.example..UserService.login(..))")
    public void loginMethod() {
    }

    @Pointcut(
        "execution(* com.example..TaskService.createTask(..)) || " +
            "execution(* com.example..TaskService.updateTask(..)) || " +
            "execution(* com.example..TaskService.deleteTask(..)) || " +
            "execution(* com.example..CommentService.save(..)) || " +
            "execution(* com.example..CommentService.updateComment(..)) || " +
            "execution(* com.example..CommentService.deleteComment(..))"
    )
    public void cudMethods() {
    }

    @Pointcut("execution(* com.example..TaskService.updateTaskStatus(..))")
    public void updateTaskStatusMethod() {
    }

    // 로그인 시 자동 로그 기록
//	@AfterReturning(pointcut = "loginMethod()", returning = "result")
//	public void logLoginMethod(Object result) {
//
//		// HttpServletRequest으로부터 요청 ip, 메서드, url
//		HttpRequestUtil.RequestInfo request = httpRequestUtil.getRequestInfo();
//
//		// 로그 저장
//		logService.saveLoginLog(request.getIp(), request.getMethod(), request.getUrl(), result);
//	}

    // 생성, 수정, 삭제 시 자동 로그 기록
    @AfterReturning(pointcut = "cudMethods()", returning = "result")
    public void logCudMethods(Object result) {

        // HttpServletRequest으로부터 요청 ip, 메서드, url, 로그인한 userid
        HttpRequestUtil.RequestInfo request = httpRequestUtil.getRequestInfo();

        // 로그 저장
        logService.saveCudLog(request.getIp(), request.getMethod(), request.getUrl(),
            request.getUserId(), result);
    }

    // 상태 변경 시 자동 로그 기록
    @Around("updateTaskStatusMethod()")
    public Object logTaskStatusChange(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Long taskId = (Long) args[0]; // 첫 번째 인자가 taskId

        // 기존 task 상태 조회
        Task beforeTask = taskService.findById(taskId); // 서비스 계층 사용
        ProcessStatus beforeStatus = beforeTask != null ? beforeTask.getStatus() : null;

        // 메서드 실행
        Object result = joinPoint.proceed();

        // 변경 후 task 상태 조회
        Task afterTask = taskService.findById(taskId);
        ProcessStatus afterStatus = afterTask != null ? afterTask.getStatus() : null;

        // 변경되었는지 비교 후 로그 기록
        Activity activity = null;
        if (Objects.equals(beforeStatus, ProcessStatus.TODO) && Objects.equals(afterStatus,
            ProcessStatus.IN_PROGRESS)) {
            activity = Activity.TASK_STATUS_TODO_TO_IN_PROGRESS;
        } else if (Objects.equals(beforeStatus, ProcessStatus.IN_PROGRESS) && Objects.equals(
            afterStatus, ProcessStatus.DONE)) {
            activity = Activity.TASK_STATUS_IN_PROGRESS_TO_DONE;
        }

        if (activity != null) {
            // HttpServletRequest으로부터 요청 ip, 메서드, url, 로그인한 userid
            HttpRequestUtil.RequestInfo request = httpRequestUtil.getRequestInfo();

            // 로그 저장
            logService.saveTaskStatusChangeLog(request.getIp(), request.getMethod(),
                request.getUrl(), request.getUserId(), taskId, activity);
        }

        return result;
    }
}
