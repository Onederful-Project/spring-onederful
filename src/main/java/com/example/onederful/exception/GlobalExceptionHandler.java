package com.example.onederful.exception;

import com.example.onederful.domain.user.dto.ApiResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.OffsetDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponseDto> handleCustomException(CustomException e){
        ErrorCode errorCode = e.getErrorCode();

        ApiResponseDto response = new ApiResponseDto(
                false,
                errorCode.getMessage(),
                null,
                OffsetDateTime.now()
        );

        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }
}
