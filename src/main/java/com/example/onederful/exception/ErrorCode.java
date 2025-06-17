package com.example.onederful.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    DUPLICATE_USER(HttpStatus.CONFLICT,"이미 존재하는 사용자입니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST,"잘못된 사용자명 또는 비밀번호입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"인증이 필요합니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED,"비밀번호가 일치하지 않습니다."),
    LOGOUT_FAIL(HttpStatus.UNAUTHORIZED,"로그아웃에 실패하였습니다.");

    private final HttpStatus status;
    private final String message;
}
