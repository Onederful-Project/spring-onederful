package com.example.onederful.domain.user.common;


import com.example.onederful.domain.user.dto.*;
import com.example.onederful.domain.user.entity.User;
import com.example.onederful.domain.user.enums.Role;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class UserMapper {

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
    
    // ResponseBody data (유저 정보)
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

    // ResponseBody date (Token)
    public static Tokeninfo token (String token){

        String newToken = token.substring(7);

        return new Tokeninfo(newToken);
    }

    // ResponseBody createdAt
    public static OffsetDateTime currentTime(){
        Date date = new Date();
        return date.toInstant().atOffset(ZoneOffset.UTC);
    }

    // 회원가입 ResponseBody
    public static ApiResponseDto signupResponse(UserResponseDto data){

        return new ApiResponseDto(
                true,
                "회원가입이 성공하였습니다",
                data,
                currentTime()
        );
    }

    // 로그인 ResponseBody 
    public static ApiResponseDto LoginResponse (Tokeninfo data){

        return new ApiResponseDto(
                true,
                "로그인이 완료되었습니다.",
                data,
                currentTime()
        );
    }

    // 회원 정보 조회
    public static ApiResponseDto selectResponse(UserResponseDto data){
        return new ApiResponseDto(
                true,
                "사용자가 정보를 조회했습니다.",
                data,
                currentTime()
        );
    }

}
