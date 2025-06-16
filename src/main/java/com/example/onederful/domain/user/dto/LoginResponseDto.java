package com.example.onederful.domain.user.dto;


import java.time.OffsetDateTime;

public class LoginResponseDto {
    private boolean success;
    private String message;
    private Tokeninfo data;
    private OffsetDateTime timestamp;
}
