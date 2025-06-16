package com.example.onederful.domain.user.controller;

import com.example.onederful.domain.user.dto.*;
import com.example.onederful.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final UserService userService;
    
    // 회원가입
    @PostMapping("/auth/register")
    public ResponseEntity<SignupResponseDto> register(@RequestBody SignupRequestDto requestDto){
        SignupResponseDto signup = userService.signup(requestDto);
        return ResponseEntity.ok(signup);
    }

    // 로그인
    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto){
        LoginResponseDto login = userService.login(requestDto);

        return ResponseEntity.ok(login);
    }

    // 현재 사용자 정보 조회
//    @GetMapping("/users/me")
//    public ResponseEntity<ApiResponseDto> select (HttpServletRequest request){
//
//    }


}
