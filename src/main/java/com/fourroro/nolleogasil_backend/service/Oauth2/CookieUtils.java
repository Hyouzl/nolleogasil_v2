package com.fourroro.nolleogasil_backend.service.Oauth2;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.util.SerializationUtils;
import java.util.Base64;
import java.util.Optional;

public class CookieUtils {

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name){
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return Optional.of(cookie);
                }
            }
        }

        return Optional.empty();
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);

        cookie.setPath("/");
        cookie.setHttpOnly(false); // ✅ JavaScript에서 접근할 수 있도록 설정
        cookie.setSecure(false);  // ✅ HTTPS가 아니므로 false 설정 (배포 시 true로 변경)
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    cookie.setValue("");
                    cookie.setPath("/"); // ✅ 기존 Path 유지
                    cookie.setMaxAge(0); // 즉시 만료
                    cookie.setHttpOnly(false); // ✅ HttpOnly 제거하여 삭제 가능하도록 설정
                    cookie.setSecure(false); // ✅ HTTPS가 아닌 환경에서도 삭제되도록 설정 (필요하면 true로 변경)
                    response.addCookie(cookie);
                    System.out.println("🔹 쿠키 삭제됨: " + name);
                }
            }
        }
    }

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setSameSite("None"); // ✅ 쿠키가 크로스 사이트에서도 유지되도록 설정
        serializer.setUseSecureCookie(false); // ✅ HTTPS가 아니라면 false
        return serializer;
    }


    public static String serialize(Object object) {
        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(object));
    }

    public static <T> T deserialize(Cookie cookie, Class<T> clazz) {
        return clazz.cast(SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.getValue())));
    }
}


