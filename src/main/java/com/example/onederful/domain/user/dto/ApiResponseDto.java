package com.example.onederful.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class ApiResponseDto {

    private boolean success;
    private String message;
    private Object data;
    private OffsetDateTime timestamp;
}
