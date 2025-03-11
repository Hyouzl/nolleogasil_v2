package com.fourroro.nolleogasil_backend.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 이 클래스는 세션 관리를 위한 인터셉터입니다.
 * 세션의 존재 여부를 확인하고 응답을 보내기 위해 존재합니다.
 * @author 장민정
 * @since 2024-01-05
 */
@Component
public class SessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        //세션 만료 여부 확인
        if(!isSessionExpired(request)){
            response.setStatus(HttpStatus.OK.value());
            return true;
        }else {
            //세션이 만료되었다면, 401 상태 코드 전달
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
    }

    public boolean isSessionExpired(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(false);

            if (session == null) {
                //세션이 존재하지 않거나 만료된 경우
                return true;
            } else {
                //세션이 유효한 경우
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
