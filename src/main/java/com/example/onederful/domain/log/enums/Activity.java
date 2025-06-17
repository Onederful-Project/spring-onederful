package com.example.onederful.domain.log.enums;

public enum Activity {
	// TASK_CREATED,
	// TASK_UPDATED,
	// TASK_DELETED,
	// TASK_STATUS_TODO_TO_IN_PROGRESS,
	// TASK_STATUS_IN_PROGRESS_TO_DONE,
	// TASK_STATUS_TODO_TO_DONE,
	// COMMENT_CREATED,
	// COMMENT_UPDATED,
	// COMMENT_DELETED,
	// USER_LOGGED_IN,
	// USER_LOGGED_OUT,
	TEST("테스트 입니다.");

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
