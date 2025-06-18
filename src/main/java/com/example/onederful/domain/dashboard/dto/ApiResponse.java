package com.example.onederful.domain.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@NoArgsConstructor
public class ApiResponse<T> {

    private boolean success;// API 호출 성공/실패 여부
    private String message;// 사용자에게 보여줄 메시지
    private T data;// 실제 데이터 (제네릭 타입)

    // JSON으로 변환될 때 날짜 형식을 지정
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private OffsetDateTime timestamp;// 응답 시간

    // 전체 필드를 초기화하는 생성자
    private ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = OffsetDateTime.now();// 현재 시간으로 자동 설정
    }

    // 성공 응답을 쉽게 만들어주는 static 메서드
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    // 실패 응답을 쉽게 만들어주는 static 메서드
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
}
