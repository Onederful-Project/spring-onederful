package com.example.onederful.domain.user.service;

import com.example.onederful.config.PasswordEncoder;
import com.example.onederful.domain.user.common.UserMapper;
import com.example.onederful.domain.user.dto.*;
import com.example.onederful.domain.user.entity.User;
import com.example.onederful.domain.user.repository.UserRepository;
import com.example.onederful.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // 회원가입
    public SignupResponseDto signup(SignupRequestDto dto){
        
        // 이메일 중복 확인
        userRepository.findByEmail(dto.getEmail()).ifPresent(
                user -> {throw new IllegalArgumentException("등록된 이메일이 존재합니다.");}
        );

        // Dto → Entity
        User user = UserMapper.user(dto);

        // 비밀번호 암호화
        user.setEncodedPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        // data
        UserResponseDto userResponseDto = UserMapper.userResponseDto(savedUser);

        return UserMapper.signupResponseDto(userResponseDto);
    }


    // 로그인
    public LoginResponseDto login(LoginRequestDto dto){
        String username = dto.getUsername();
        String password = dto.getPassword();

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("아이디가 일치하지 않습니다.")
        );
        
        if(!passwordEncoder.matches(password,user.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }

        // JWT Token
        String token = jwtUtil.generateToken(user);

        // data
        Tokeninfo data = UserMapper.token(token);

        return UserMapper.LoginResponseDto(data);
    }
}
