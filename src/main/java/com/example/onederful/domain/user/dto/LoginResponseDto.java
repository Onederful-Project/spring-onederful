package com.example.onederful.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LoginResponseDto {
    private boolean success;
    private String message;
    private Tokeninfo data;
    private OffsetDateTime timestamp;
}
