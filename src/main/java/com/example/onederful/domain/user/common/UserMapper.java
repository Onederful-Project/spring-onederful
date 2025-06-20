package com.example.onederful.domain.user.common;

import com.example.onederful.domain.user.dto.*;
import com.example.onederful.domain.user.entity.User;
import com.example.onederful.domain.user.enums.Role;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;


public class UserMapper {

    // Dto → Entity
    public static User user (RequestDto dto){
        return User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .name(dto.getName())
                .role(Role.USER)
                .isDeleted(false)
                .build();
    }
    
    // ResponseBody data (유저 정보) (Entity → Dto)
    public static UserResponseDto data(User user){
        // LocalDateTime -> OffsetDateTime
        OffsetDateTime createAt = user.getCreatedAt().atOffset(ZoneOffset.UTC);

        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getName(),
                user.getRole(),
                createAt
        );
    }


}
