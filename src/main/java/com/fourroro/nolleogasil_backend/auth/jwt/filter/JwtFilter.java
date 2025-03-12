package com.fourroro.nolleogasil_backend.auth.jwt.filter;

import com.fourroro.nolleogasil_backend.apiPayLoad.Exception.InvalidJWTException;
import com.fourroro.nolleogasil_backend.auth.jwt.util.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


/**
 *
 * JwtFilter는 모든 요청을 가로채서 Authorization 헤더에 있는 JWT를 확인합니다.
 * 헤더에 JWT가 있다면 TokenProvider의 getAuthentication() 메서드를 호출하여 사용자 인증 정보를 얻습니다.
 * 이 인증 정보를 SecurityContextHolder에 저장하여 이후 보안 검사가 가능하도록 합니다.
 * **/

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;
    // ✅ 1초 동안 JWT 검증 결과를 캐싱
    private static final Map<String, Authentication> authCache = new ConcurrentHashMap<>();
    private static final Map<String, Long> cacheExpiry = new ConcurrentHashMap<>();


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String requestURI = request.getRequestURI();

        // /api/users/refresh 요청은 JWT 검증을 건너뜀
        if (requestURI.equals("/api/users/refresh")) {
            System.out.println("refresh 요청");
            filterChain.doFilter(request, response);
            return;
        }
        if (requestURI.equals("/api/user/login")) {
            System.out.println("로그인요청");
            filterChain.doFilter(request, response);
            return;
        }
        log.debug("Received request: {} {}", request.getMethod(), request.getRequestURI());
        if (requestURI.startsWith("/ws/info")) { // 🔹 정확한 비교를 위해 startsWith 사용
            System.out.println("웹소켓 접속 요청");

            log.debug("Received request: {} {}", request.getMethod(), request.getRequestURI());
            log.debug("Query String: {}", request.getQueryString()); // 🔹 쿼리 스트링 확인
            log.debug("Full URL: {}", request.getRequestURL()); // 🔹 전체 URL 확인

            String token = request.getParameter("token"); // WebSocket URL 쿼리 파라미터로 JWT 전달
            System.out.println("token: " + token);

            if (token == null || token.isEmpty()) {
                log.warn("토큰이 없습니다. WebSocket 인증 실패");
            } else {
                log.debug("Extracted token: {}", token);
            }

            filterChain.doFilter(request, response);
            return;
        }


        // 1. Request Header 에서 토큰을 꺼냄
        String jwt = resolveToken(request);
        System.out.println("JwtToke" + jwt);
        if (jwt == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            return;
        }

        try {

            // 2. validateToken 으로 토큰 유효성 검사
            // 정상 토큰이면 해당 토큰으로 Authentication 을 가져와서 SecurityContext 에 저장
            if (StringUtils.hasText(jwt)) {
                if (Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:" + jwt))) {
                    throw new ServletException("이미 로그아웃 된 토큰입니다.");
                }
                String userId = (String) redisTemplate.opsForValue().get("auth:" + jwt);
                if (userId == null) {
                    // JWT 검증 후 Authentication 생성 및 SecurityContext 에 저장
                    // JWT 만료 시, 401 에러 반환
                    tokenProvider.validateToken(jwt);
                    Authentication authentication = tokenProvider.getAuthentication(jwt);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    // 4. Redis에 인증 정보 캐싱 (JWT 만료 시간과 동일하게 설정)
                    String subject = tokenProvider.getClaims(jwt).getSubject(); // userId
                    redisTemplate.opsForValue().set("auth:" + jwt, subject, tokenProvider.getExpirationTime(jwt), TimeUnit.MILLISECONDS);

                    // 1초 동안 캐싱
                    authCache.put(jwt, authentication);
                    cacheExpiry.put(jwt, System.currentTimeMillis() + 1000);
                } else {
                    // Redis에 userId가 있으면 Authentication 재생성하여 SecurityContext 설정
                    Authentication authentication = tokenProvider.getAuthenticationFromUserId(userId);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    // 1초 동안 캐싱
                    authCache.put(jwt, authentication);
                    cacheExpiry.put(jwt, System.currentTimeMillis() + 1000);
                }
            }
            filterChain.doFilter(request, response);
        } catch (InvalidJWTException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }

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
