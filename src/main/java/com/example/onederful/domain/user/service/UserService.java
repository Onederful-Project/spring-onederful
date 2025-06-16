package com.example.onederful.domain.user.service;

import com.example.onederful.config.PasswordEncoder;
import com.example.onederful.domain.user.common.UserMapper;
import com.example.onederful.domain.user.dto.*;
import com.example.onederful.domain.user.entity.User;
import com.example.onederful.domain.user.repository.UserRepository;
import com.example.onederful.exception.CustomException;
import com.example.onederful.exception.ErrorCode;
import com.example.onederful.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // 회원가입
    public ApiResponseDto signup(RequestDto dto){
        
        // 이메일 중복 확인
        userRepository.findByEmail(dto.getEmail()).ifPresent(
                user -> {throw new CustomException(ErrorCode.DUPLICATE_USER);}
        );

        // Dto → Entity
        User user = UserMapper.user(dto);

        // 비밀번호 암호화
        user.setEncodedPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        // ResponseBody data(유저 정보)
        UserResponseDto data = UserMapper.data(savedUser);

        return UserMapper.signupResponse(data);
    }


    // 로그인
    public ApiResponseDto login(RequestDto dto){
        String username = dto.getUsername();
        String password = dto.getPassword();

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(ErrorCode.BAD_REQUEST)
        );
        
        if(!passwordEncoder.matches(password,user.getPassword())){
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        // JWT Token
        String token = jwtUtil.generateToken(user);

        // ResponseBody data(Token)
        Tokeninfo data = UserMapper.token(token);

        return UserMapper.LoginResponse(data);
    }


    // 회원 정보 조회
    public ApiResponseDto select(HttpServletRequest request){
        
        // 요청 헤더에서 토큰 가져오기
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);

        // 토큰에서 Id 가져오기
        Long userId = jwtUtil.extractId(token);

        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.UNAUTHORIZED)
        );

        UserResponseDto data = UserMapper.data(user);

        return UserMapper.selectResponse(data);
    }
}
