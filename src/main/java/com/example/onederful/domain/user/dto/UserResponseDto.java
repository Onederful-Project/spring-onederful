package com.example.onederful.domain.user.dto;

import com.example.onederful.domain.user.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;


import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private String name;
    private Role role;
    private OffsetDateTime createdAt;
}
