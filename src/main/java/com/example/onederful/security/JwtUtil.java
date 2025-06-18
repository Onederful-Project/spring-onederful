package com.example.onederful.security;

import com.example.onederful.domain.user.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    // JWT Token 접두사
    public final static String BEARER_PREFIX = "Bearer ";

    // JWT Token 만료시간
    @Value("${jwt.expiration}")
    public Long expirationTime;

    // JWT 서명 알고리즘
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // 비밀 키
    @Value("${jwt.secret.key}")
    private String secretKey;

    // 실제 서명에서 사용할 키 객체
    private Key key;


    /**
     * 빈 초기화 메서드
     * - 애플리케이션 실행 시 비밀키를 Base64로 디코딩 하여 key 객체를 초기화
     */
    @PostConstruct
    public void init(){
        byte [] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }


    /**
     * JWT 토큰 생성
     * @param user User Entity
     * @return 생성된 JWT 토큰
     */
    public String generateToken(User user){

        Long id = user.getId();
        String username = user.getUsername();
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)
                        .claim("id",id)
                        .setIssuedAt(date)
                        .setExpiration(new Date(date.getTime()+ expirationTime))
                        .signWith(key,signatureAlgorithm)
                        .compact();
    }


    /**
     * JWT 토큰 유효성 검증
     * @param token JWT 토큰
     * @return 토큰의 유효성 여부 (true : 유효, false : 유효하지 않음)
     */
    public String validateToken(String token){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return null; // 유효함
        } catch (SecurityException | MalformedJwtException e) {
            return "유효하지 않은 JWT 서명입니다.";
        } catch (ExpiredJwtException e) {
            return "만료된 JWT 토큰입니다.";
        } catch (UnsupportedJwtException e) {
            return "지원되지 않는 JWT 토큰입니다.";
        } catch (IllegalArgumentException e) {
            return "잘못된 JWT 토큰입니다.";
        }
    }

    /**
     * Token에 존재하는 모든 클레임(페이로드 값)을 추출
     * @param token 검증된 JWT 토큰 (로그인 한 상태)
     * @return 클라임 객체
     */
    public Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Token에 저장된 ID(기본키) 가져오기
     * @param request Request
     * @return ID값
     */
    public Long extractId(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        return extractAllClaims(token).get("id", Long.class);
    }




}