package com.example.onederful.domain.log.dto;

import java.time.LocalDateTime;

import com.example.onederful.domain.log.entity.Log;

import lombok.Getter;

@Getter
public class LogResponseDto {
	LocalDateTime createdAt;
	String userName;
	String activityStr;
	Long targetID;
	String logMessage;

	public LogResponseDto(
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

	public static LogResponseDto of(Log log) {
		return new LogResponseDto(
			log.getCreatedAt(),
			log.getUser().getName(),
			log.getActivity().toString(),
			log.getTargetId(),
			log.getActivity().getLogMessage());
	}
}
