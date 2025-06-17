package com.example.onederful.filter;

import com.example.onederful.security.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

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
            log.info("JWT 토큰이 필요합니다.");
            response.sendError(response.SC_UNAUTHORIZED,"JWT 토큰이 필요합니다.");
            return;
        }

        // "Bearer" 빼고 확인
        String jwt = authorizationHeader.substring(7);
        
        // 토큰 검증 유무 확인
        if(!jwtUtil.validateToken(jwt)){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"error\": \"Unauthorized\"}");
            return;
        }

        filterChain.doFilter(servletRequest,servletResponse);
    }
}
