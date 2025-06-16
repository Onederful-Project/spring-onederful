package com.example.onederful.domain.user.dto;

import com.example.onederful.domain.user.enums.Role;

import java.time.OffsetDateTime;

public class UserResponseDto {
    private String id;
    private String username;
    private String email;
    private Role role;
    private OffsetDateTime createdAt;
}
