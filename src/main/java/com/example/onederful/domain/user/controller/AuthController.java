package com.example.onederful.domain.user.controller;

import com.example.onederful.common.ApiResponseDto;
import com.example.onederful.domain.user.common.LoginGroup;
import com.example.onederful.domain.user.common.PasswordGroup;
import com.example.onederful.domain.user.common.SignupGroup;
import com.example.onederful.domain.user.common.UserMapper;
import com.example.onederful.domain.user.dto.*;
import com.example.onederful.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.RequestInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/auth/register")
    public ResponseEntity<ApiResponseDto> register(@Validated(SignupGroup.class)  @RequestBody RequestDto requestDto){

        UserResponseDto signup = userService.signup(requestDto);

        ApiResponseDto success = ApiResponseDto.success("회원가입이 성공하였습니다.", signup);

        return ResponseEntity.status(HttpStatus.OK).body(success);
    }

    // 로그인
    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponseDto> login(@Validated(LoginGroup.class)  @RequestBody RequestDto requestDto){

        Tokeninfo token = userService.login(requestDto);

        ApiResponseDto success = ApiResponseDto.success("로그인이 완료되었습니다.", token);

        return ResponseEntity.status(HttpStatus.OK).body(success);
    }

    // 현재 사용자 정보 조회
    @GetMapping("/users/me")
    public ResponseEntity<ApiResponseDto> select (HttpServletRequest request){

        UserResponseDto select = userService.select(request);

        ApiResponseDto success = ApiResponseDto.success("사용자가 정보를 조회했습니다.", select);

        return ResponseEntity.status(HttpStatus.OK).body(success);
    }

    // 회원 탈퇴 (계정 삭제)
    @PostMapping("/auth/withdraw")
    public ResponseEntity<ApiResponseDto> withdraw (HttpServletRequest request,
                                                    @Validated(PasswordGroup.class)  @RequestBody RequestDto dto){

        userService.withdraw(request,dto);

        ApiResponseDto success = ApiResponseDto.success("회원탈퇴가 완료되었습니다.", null);

        return ResponseEntity.status(HttpStatus.OK).body(success);
    }

    // 모든 회원 정보 조회
    @GetMapping("/users")
    public ResponseEntity<ApiResponseDto> selectAll(){
        List<UserResponseDto> selectAll = userService.selectAll();

        ApiResponseDto success = ApiResponseDto.success("요청이 성공적으로 처리되었습니다.",selectAll);

        return ResponseEntity.status(HttpStatus.OK).body(success);
    }



}
