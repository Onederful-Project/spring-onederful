package com.example.onederful.domain.log;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.onederful.domain.log.enums.Method;
import com.example.onederful.security.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HttpRequestUtil {

	private final JwtUtil jwtUtil;

	// HttpServletRequest으로부터 요청 ip, 메서드, url, userId
	public RequestInfo getRequestInfo() {
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attrs == null) {
			throw new IllegalStateException("Request attributes not found");
		}
		HttpServletRequest request = attrs.getRequest();
		if (request == null) {
			throw new IllegalStateException("HttpServletRequest not found");
		}

		// 요청한 사용자의 ip
		String ip = request.getRemoteAddr();

		// 요청 메서드
		String method = request.getMethod();
		Method enumMethod = Method.valueOf(method);

		// 요청 url
		String url = request.getRequestURI();

		// 요청 헤더의 토큰으로부터 요청한 사용자의 userId
		String authorizationHeader = request.getHeader("Authorization");
		String token = authorizationHeader.substring(7);
		Long userId = jwtUtil.extractId(token);

		return new RequestInfo(ip, enumMethod, url, userId);
	}

	// 반환용 클래스
	@Getter
	@AllArgsConstructor
	public static class RequestInfo {
		private final String ip;
		private final Method method;
		private final String url;
		private final Long userId;
	}
}
