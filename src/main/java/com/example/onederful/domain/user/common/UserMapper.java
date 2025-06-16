package com.example.onederful.domain.user.common;


import com.example.onederful.domain.user.dto.*;
import com.example.onederful.domain.user.entity.User;
import com.example.onederful.domain.user.enums.Role;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class UserMapper {

    public static User user (SignupRequestDto dto){
        return User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .name(dto.getName())
                .role(Role.USER)
                .isDeleted(false)
                .build();
    }
    
    // ResponseBody에서 data
    public static UserResponseDto userResponseDto (User user){
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

    // 회원가입 ResponseBody
    public static SignupResponseDto signupResponseDto(UserResponseDto data){
        Date date = new Date();
        OffsetDateTime timestamp = date.toInstant().atOffset(ZoneOffset.UTC);

        return new SignupResponseDto(
                true,
                "회원가입이 성공하였습니다",
                data,
                timestamp
        );
    }

    // 토큰 담기
    public static Tokeninfo token (String token){

        String newToken = token.substring(7);

        return new Tokeninfo(newToken);
    }
    
    public static LoginResponseDto LoginResponseDto (Tokeninfo data){

        Date date = new Date();
        OffsetDateTime timestamp = date.toInstant().atOffset(ZoneOffset.UTC);

        return new LoginResponseDto(
                true,
                "로그인이 완료되었습니다.",
                data,
                timestamp
        );

    }

}
