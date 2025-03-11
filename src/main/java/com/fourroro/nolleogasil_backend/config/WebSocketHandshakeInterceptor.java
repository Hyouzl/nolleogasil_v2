package com.fourroro.nolleogasil_backend.config;

import com.fourroro.nolleogasil_backend.apiPayLoad.Exception.InvalidJWTException;
import com.fourroro.nolleogasil_backend.auth.jwt.util.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeFailureException;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    private final TokenProvider jwtTokenProvider;

    public WebSocketHandshakeInterceptor(TokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        System.out.println("websocker interceptor");
        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest req = servletRequest.getServletRequest();
            String token = req.getParameter("token"); // ✅ WebSocket URL 쿼리 파라미터로 JWT 전달

            System.out.println("token: " + token);
            if (token != null) {
                try {
                   jwtTokenProvider.validateToken(token);
                } catch (InvalidJWTException e) {
                    // 401 응답을 보내고 WebSocket 연결을 중단함
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    throw new HandshakeFailureException("401 Unauthorized: 만료된 JWT 토큰입니다.");
                }
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                attributes.put("auth", auth); // ✅ 인증 정보 저장
                return true;
            }
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        // 핸드셰이크 후 특별한 처리 없음
    }
}
