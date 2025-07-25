package com.example.onederful.domain.log;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.onederful.domain.log.enums.Method;
import com.example.onederful.exception.CustomException;
import com.example.onederful.exception.ErrorCode;
import com.example.onederful.security.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HttpRequestUtil {

	private final JwtUtil jwtUtil;

	// HttpServletRequest으로부터 요청 ip, 메서드, url, 로그인한 userId
	public RequestInfo getRequestInfo() {
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attrs == null) {
			throw new CustomException(ErrorCode.INVALID_OR_EXPIRED_REQUEST);
		}
		HttpServletRequest request = attrs.getRequest();
		if (request == null) {
			throw new CustomException(ErrorCode.INVALID_OR_EXPIRED_REQUEST);
		}

		// 요청한 사용자의 ip
		String ip = request.getRemoteAddr();

		// 요청 메서드
		String method = request.getMethod();
		Method enumMethod = Method.valueOf(method);

		// 요청 url
		String url = request.getRequestURI();

		// 토큰으로부터 요청한 사용자의 userId
		Long userId = null;
		// 로그인, 회원가입 등 토큰 체크 안 할 URL 처리
		if (!url.startsWith("/api/auth/login") && !url.startsWith("/api/auth/register")) {
			userId = jwtUtil.extractId(request);
		}

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
