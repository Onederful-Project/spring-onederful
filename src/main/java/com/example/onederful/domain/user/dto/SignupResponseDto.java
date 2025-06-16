package com.example.onederful.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;


import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class SignupResponseDto {
    private boolean success;
    private String message;
    private UserResponseDto data;
    private OffsetDateTime timestamp;

}
