package com.fourroro.nolleogasil_backend.service.Oauth2;

import com.fourroro.nolleogasil_backend.auth.jwt.JwtTokenResponseDto;
import com.fourroro.nolleogasil_backend.auth.jwt.util.TokenProvider;
import com.fourroro.nolleogasil_backend.entity.users.PrincipalDetails;
import com.fourroro.nolleogasil_backend.repository.users.CookieAuthorizationRequestRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Log4j2
@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("인증 성공");
        // OAuth2User에서 필요한 사용자 정보 확인
        PrincipalDetails oAuth2User = (PrincipalDetails) authentication.getPrincipal();
        log.info("OAuth2 인증 성공. 사용자: {}", oAuth2User.getAttributes());
        log.info(oAuth2User.getName());

        JwtTokenResponseDto jwtTokenResponseDto = tokenProvider.generateTokenDto(authentication,"SOCIAL");

        // Refresh Token을 HttpOnly 쿠키에 저장
       /** Cookie cookie = new Cookie("refreshToken", jwtTokenResponseDto.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // HTTPS에서만 사용 가능
        cookie.setPath("/"); // 모든 경로에서 사용 가능
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7일 동안 유효
        response.addCookie(cookie); **/

        //  Refresh Token을 레디스에 저장
       String key = "refreshToken" + oAuth2User.getUserId();
        long expiration = 7 * 24 * 60 * 60; // 7일 (초 단위)
        redisTemplate.opsForValue().set(key, jwtTokenResponseDto.getRefreshToken(), expiration, TimeUnit.SECONDS);

        // 리다이렉트 URL 생성
        String redirectUrl = makeRedirectUrl(jwtTokenResponseDto.getAccessToken(), oAuth2User.getUserId());
        // 프론트엔드로 리다이렉트
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);

    }

    private String makeRedirectUrl(String accessToken, Long userId) {
        return UriComponentsBuilder.fromUriString("http://localhost:3000/oauth2/redirect")
                .queryParam("accessToken", accessToken)
                .queryParam("userId", userId)
                .build().toUriString();
    }
}

