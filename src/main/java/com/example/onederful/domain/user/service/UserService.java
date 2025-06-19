package com.example.onederful.domain.user.service;

import com.example.onederful.config.PasswordEncoder;
import com.example.onederful.domain.user.common.UserMapper;
import com.example.onederful.domain.user.dto.RequestDto;
import com.example.onederful.domain.user.dto.Tokeninfo;
import com.example.onederful.domain.user.dto.UserResponseDto;
import com.example.onederful.domain.user.entity.User;
import com.example.onederful.domain.user.repository.UserRepository;
import com.example.onederful.exception.CustomException;
import com.example.onederful.exception.ErrorCode;
import com.example.onederful.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;


    // 회원가입
    @Transactional
    public UserResponseDto signup(RequestDto dto){
        
        // 이메일 중복 확인
        if(userRepository.existsByEmail(dto.getEmail())){
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }


        // 아이디 중복 확인
        if(userRepository.existsByUsername(dto.getUsername())){
            throw new CustomException(ErrorCode.DUPLICATE_USER);
        }

        // Dto → Entity
        User user = UserMapper.user(dto);

        // 비밀번호 암호화
        user.setEncodedPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        // ResponseBody data(유저 정보)
        return  UserMapper.data(savedUser);
    }


    // 로그인
    public Tokeninfo login(RequestDto dto){
        String username = dto.getUsername();
        String password = dto.getPassword();


        User user = userRepository.findByUsername(username).orElseThrow(
            () -> new CustomException(ErrorCode.BAD_REQUEST)
        );

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        // JWT Token
        String token = jwtUtil.generateToken(user);

        // ResponseBody data(Token)
        return token(token);
    }


    // 회원 정보 조회
    @Transactional
    public UserResponseDto select(HttpServletRequest request){

        // 토큰에서 Id 가져오기
        Long userId = jwtUtil.extractId(request);

        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.UNAUTHORIZED)
        );

        return UserMapper.data(user);
    }


    // 회원 탈퇴
    @Transactional
    public void withdraw(HttpServletRequest request , RequestDto dto){
        // 토큰에서 Id 가져오기
        Long userId = jwtUtil.extractId(request);

        // 비밀번호
        String password = dto.getPassword();

        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.UNAUTHORIZED)
        );

        if (!passwordEncoder.matches(password,user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        user.delete();
    }


    // 모든 회원 정보 조회
    public List<UserResponseDto> selectAll(){

        List<UserResponseDto> result = new ArrayList<>();

        List<User> all = userRepository.findAll();

        for(User user : all){
            result.add(UserMapper.data(user));
        }

        return result;

    }




    // ResponseBody date (Token)
    private Tokeninfo token (String token){

        String newToken = token.substring(7);

        return new Tokeninfo(newToken);
    }


}
