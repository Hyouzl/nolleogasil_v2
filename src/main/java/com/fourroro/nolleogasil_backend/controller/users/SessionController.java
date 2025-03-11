package com.fourroro.nolleogasil_backend.controller.users;

import com.fourroro.nolleogasil_backend.config.SessionInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 이 클래스는 세션 관리를 위한 컨트롤러입니다.
 * @author 장민정
 * @since 2024-01-05
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class SessionController {
    private final SessionInterceptor sessionInterceptor;

    @GetMapping("/session/check")
    public ResponseEntity<String> checkerSession(HttpServletRequest request){
        if (sessionInterceptor.isSessionExpired(request)) {
            //세션이 존재하지 않거나 만료된 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session expired");
        } else {
            // 세션이 유효한 경우
            return ResponseEntity.ok("Session valid");
        }
    }
}
