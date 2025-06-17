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

    // 성공 응답 static 메서드
    public static ApiResponseDto success(String message,Object date){
        return new ApiResponseDto(true, message , date, OffsetDateTime.now());
    }

    // 실패 응답 static 메서드
    public static ApiResponseDto error(String message){
        return new ApiResponseDto(false, message, null, OffsetDateTime.now());
    }

}
