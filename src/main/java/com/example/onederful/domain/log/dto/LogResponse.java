package com.example.onederful.domain.log.dto;

import java.time.LocalDateTime;

import com.example.onederful.domain.log.entity.Log;

import lombok.Getter;

@Getter
public class LogResponse {
	LocalDateTime createdAt;
	String userName;
	String activityStr;
	Long targetID;
	String logMessage;

	public LogResponse(
		LocalDateTime createdAt,
		String userName,
		String activityStr,
		Long targetID,
		String logMessage)
	{
		this.createdAt = createdAt;
		this.userName = userName;
		this.activityStr = activityStr;
		this.targetID = targetID;
		this.logMessage = logMessage;
	}

	public static LogResponse of(Log log) {
		return new LogResponse(
			log.getCreatedAt(),
			log.getUser().getName(),
			log.getActivity().toString(),
			log.getTargetId(),
			log.getActivity().getLogMessage());
	}
}
