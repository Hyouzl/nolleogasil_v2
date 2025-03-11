package com.fourroro.nolleogasil_backend.service.Oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private final HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        // ✅ 기존 세션을 유지하도록 설정
        requestCache.saveRequest(request, response);
        getRedirectStrategy().sendRedirect(request, response, "/login?error=true");
        log.error("OAuth2 로그인 실패: {}", exception.getMessage());
        log.error("요청 세션 ID: {}", request.getSession().getId());
        log.error("쿠키 목록: {}", Arrays.toString(request.getCookies()));
        response.sendRedirect("/login?error=true"); // 실패 시 리다이렉트
    }

}

