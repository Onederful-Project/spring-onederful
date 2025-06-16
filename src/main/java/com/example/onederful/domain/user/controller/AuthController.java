package com.example.onederful.domain.user.controller;

import com.example.onederful.domain.user.common.LoginGroup;
import com.example.onederful.domain.user.common.SignupGroup;
import com.example.onederful.domain.user.dto.*;
import com.example.onederful.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final UserService userService;
    
    // 회원가입
    @PostMapping("/auth/register")
    public ResponseEntity<ApiResponseDto> register(@Validated(SignupGroup.class) @RequestBody RequestDto requestDto){

        ApiResponseDto signup = userService.signup(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(signup);
    }

    // 로그인
    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponseDto> login(@Validated(LoginGroup.class) @RequestBody RequestDto requestDto){

        ApiResponseDto login = userService.login(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(login);
    }

    // 현재 사용자 정보 조회
    @GetMapping("/users/me")
    public ResponseEntity<ApiResponseDto> select (HttpServletRequest request){

        ApiResponseDto select = userService.select(request);

        return ResponseEntity.status(HttpStatus.OK).body(select);
    }


}
