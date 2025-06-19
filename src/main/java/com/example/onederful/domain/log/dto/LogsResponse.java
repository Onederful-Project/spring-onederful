package com.example.onederful.domain.log.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LogsResponse {

	private List<LogResponse> content;
	private Long totalElements;
	private Long totalPages;
	private Long size;
	private Long number;
}
