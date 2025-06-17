package com.example.onederful.exception;

import com.example.onederful.domain.comment.ApiResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponseDto> handleCustomException(CustomException e){
        ErrorCode errorCode = e.getErrorCode();

        ApiResponseDto response = ApiResponseDto.error(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }
}
