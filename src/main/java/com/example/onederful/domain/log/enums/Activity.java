package com.example.onederful.domain.log.enums;

public enum Activity {
	TASK_CREATED("새로운 작업이 생성되었습니다."),
	TASK_UPDATED("작업이 수정되었습니다."),
	TASK_DELETED("작업이 삭제되었습니다."),
	TASK_STATUS_TODO_TO_IN_PROGRESS("작업이 TODO에서 IN_PROGRESS로 변경되었습니다."),
	TASK_STATUS_IN_PROGRESS_TO_DONE("작업이 IN_PROGRESS에서 DONE으로 변경되었습니다."),
	TASK_STATUS_TODO_TO_DONE("작업이 TODO에서 DONE으로 변경되었습니다."),
	// COMMENT_CREATED,
	// COMMENT_UPDATED,
	// COMMENT_DELETED,
	USER_LOGGED_IN("로그인 하였습니다.");
	// USER_LOGGED_OUT("로그아웃 하였습니다.")

	private final String logMessage;

	// 생성자
	Activity(String logMessage) {
		this.logMessage = logMessage;
	}

	// getter
	public String getLogMessage() {
		return logMessage;
	}
}
