/*
package com.fourroro.nolleogasil_backend.service.users;
import com.fourroro.nolleogasil_backend.dto.users.KakaoInfo;
import com.fourroro.nolleogasil_backend.dto.users.KakaoTokens;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;

/**
 * 이 클래스는 카카오 로그인을 위한 Service class입니다.
 * @author 장민정
 * @since 2024-01-05
 *
@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService{
    private final KakaoClient client;

    @Value("${spring.security.oauth2.client.provider.kakao.authorization-uri}")
    private String kakaoAuthUrl;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String kakaoUserApiUrl;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String restApiKey;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUrl;

    public KakaoInfo getInfo(final String code){
        final KakaoTokens token = getToken(code);

        //token 확인
        log.debug("token = {}", token);

        try{
            return client.getInfo(new URI(kakaoUserApiUrl), token.getTokenType() + " " + token.getAccessToken());
        }catch (Exception e){
            log.debug("error 발생! ", e);
            return KakaoInfo.fail();
        }
    }

    private KakaoTokens getToken(final String code) {
        try {
            return client.getToken(new URI(kakaoAuthUrl), restApiKey, redirectUrl, code, "authorization_code");
        } catch (Exception e) {
            log.debug("error 발생! ", e);
            return KakaoTokens.fail();
        }
    }
}
*/
