package com.fourroro.nolleogasil_backend.auth.session;

import com.fourroro.nolleogasil_backend.auth.jwt.util.TokenProvider;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.io.IOException;

@RequiredArgsConstructor
public class CookieSessionFilter implements Filter {
    private static final String OAUTH2_AUTH_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private final TokenProvider tokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpSession session = httpRequest.getSession(false); // 기존 세션 가져오기 (없으면 null)

        // 1. Request Header 에서 토큰을 꺼냄
        String jwt = resolveToken((HttpServletRequest) servletRequest);
        System.out.println("JwtToke" + jwt);

        // 2. validateToken 으로 토큰 유효성 검사
        // 정상 토큰이면 해당 토큰으로 Authentication 을 가져와서 SecurityContext 에 저장
        if (StringUtils.hasText(jwt)) {
            tokenProvider.validateToken(jwt);
            Authentication authentication = tokenProvider.getAuthentication(jwt);

            // 기존 SecurityContext가 존재하는 경우, 기존 세션을 유지하도록 수정
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        boolean hasOAuth2Cookie = false;
        if (httpRequest.getCookies() != null) {
            for (Cookie cookie : httpRequest.getCookies()) {
                if (OAUTH2_AUTH_REQUEST_COOKIE_NAME.equals(cookie.getName())) {
                    hasOAuth2Cookie = true;
                    break;
                }
            }
        }

        // 쿠키가 없으면 세션을 새로 만들지 않음
        if (!hasOAuth2Cookie && session != null) {
            session.invalidate();
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    // Request Header 에서 토큰 정보를 꺼내오기
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.split(" ")[1].trim();
        }
        return null;
    }
}
