package com.example.onederful.filter;

import com.example.onederful.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SecurityException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.time.OffsetDateTime;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter implements Filter {

    private final JwtUtil jwtUtil;

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestURI = request.getRequestURI();

        String authorizationHeader = request.getHeader("Authorization");


        // 회원가입, 로그인 경우
        if(requestURI.startsWith("/api/auth/register") || requestURI.startsWith("/api/auth/login")){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        // 토큰 존재 유무 확인
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
            errorResponse(response,HttpServletResponse.SC_UNAUTHORIZED,"인증이 필요합니다");
            return;
        }

        // "Bearer" 빼고 확인
        String jwt = authorizationHeader.substring(7);
        
        // 토큰 검증 유무 확인 (응답 형태가 올바르지 못한 상태)
        try {
            jwtUtil.validateToken(jwt);
        }
        catch (SignatureException e) {
            errorResponse(response, HttpServletResponse.SC_FORBIDDEN, "유효하지 않은 JWT 서명입니다.");
        } catch (SecurityException | MalformedJwtException e){
            errorResponse(response,HttpServletResponse.SC_FORBIDDEN,"잘못된 JWT 토큰 형식입니다.");
        }catch (ExpiredJwtException e){
            errorResponse(response,HttpServletResponse.SC_FORBIDDEN,"Expired JWT token, 만료된 JWT token 입니다.");
        }catch (UnsupportedJwtException e){
            errorResponse(response,HttpServletResponse.SC_FORBIDDEN,"Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        }catch (IllegalArgumentException e){
            errorResponse(response,HttpServletResponse.SC_FORBIDDEN,"JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }


        filterChain.doFilter(servletRequest,servletResponse);
    }

    private void errorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=utf-8");

        String json = "{" +
                "\"success\" : false," +
                "\"message\": \""+ message + "\"," +
                "\"data\" : null," +
                "\"timestamp\" : \"" + OffsetDateTime.now() + "\"" +
                "}";

        response.getWriter().write(json);
    }

//    private void errorResponse(HttpServletResponse response, String message) throws IOException{
//        ObjectMapper objectMapper = new ObjectMapper();
//        response.setCharacterEncoding("utf-8");
//        response.setStatus(HttpStatus.UNAUTHORIZED.value());
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.getWriter().write(objectMapper.writeValueAsString(Response.builder));
//    }
}
