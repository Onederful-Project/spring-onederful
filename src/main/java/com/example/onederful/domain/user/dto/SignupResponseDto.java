package com.example.onederful.domain.user.dto;

import java.time.OffsetDateTime;

public class SignupResponseDto {
    private boolean success;
    private String message;
    private UserResponseDto data;
    private OffsetDateTime timestamp;

}
