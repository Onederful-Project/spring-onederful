package com.example.onederful.domain.comment.dto;

import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
public class ResponseDto<T> {
    private final boolean success;
    private final String message;
    private final T dataDto;
    private final OffsetDateTime timestamp;

    public ResponseDto(boolean success, String message, T dataDto) {
        this.success = success;
        this.message = message;
        this.dataDto = dataDto;
        this.timestamp = OffsetDateTime.now();
    }

    public static <T> ResponseDto<T> success(String message, T data){
        return new ResponseDto<>(true, message, data);
    }

    public static <T> ResponseDto<T> fail(String message){
        return new ResponseDto<>(false, message, null);
    }
}
