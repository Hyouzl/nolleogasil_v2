package com.fourroro.nolleogasil_backend.controller.users;

import com.fourroro.nolleogasil_backend.auth.jwt.JwtTokenResponseDto;
import com.fourroro.nolleogasil_backend.auth.jwt.util.TokenProvider;
import com.fourroro.nolleogasil_backend.service.users.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AuthController {


    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final RedisTemplate<String, Object> redisTemplate;

    @PostMapping("/logout")
    @Transactional
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authorizationHeader) {
        // JWT 토큰 추출
        String token = authorizationHeader.replace("Bearer ", "");
        // 토큰에서 사용자 ID 가져오기
        Long userId = Long.parseLong(tokenProvider.getClaims(token).getSubject());

        // Redis에서 리프레시 토큰 삭제 (로그아웃 처리)
        String refreshTokenKey = "refreshToken" + userId;
        redisTemplate.delete(refreshTokenKey);

        System.out.println("refreshTokenKey" + refreshTokenKey + "로그아웃 처리");

        // 액세스 토큰을 블랙리스트에 추가하여 더 이상 사용되지 않도록 설정
        long expirationTime = tokenProvider.getExpirationTime(token);
        if (expirationTime > 0) {
            redisTemplate.opsForValue().set("blacklist:" + token, "expired", expirationTime, TimeUnit.MILLISECONDS);
        }

        return ResponseEntity.ok("Logged out successfully");
    }
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestParam(name = "userId") Long userId) {
        // 쿠키에서 Refresh Token 가져오기
       /**  Cookie[] cookies = request.getCookies();
        String refreshToken = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshToken")) {
                    refreshToken = cookie.getValue();
                    System.out.println(refreshToken);
                }
            }
        } **/

        System.out.println("userId" + userId);

        String storedRefreshToken = (String) redisTemplate.opsForValue().get("refreshToken" + userId);

        System.out.println(storedRefreshToken);
        if (storedRefreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh Token is missing");
        }

        // Refresh Token 검증
        tokenProvider.validateToken(storedRefreshToken);

        // Refresh Token으로 사용자 정보 조회
        Authentication authentication = tokenProvider.getAuthentication(storedRefreshToken);

        // 새로운 Access Token 발급
       JwtTokenResponseDto tokenResponseDto = tokenProvider.generateTokenDto(authentication, "SOCIAL");

        return ResponseEntity.ok(tokenResponseDto.getAccessToken());
    }


}

