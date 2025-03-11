package com.fourroro.nolleogasil_backend.service.users;

import com.fourroro.nolleogasil_backend.config.UsersConfig;
import com.fourroro.nolleogasil_backend.dto.users.KakaoInfo;
import com.fourroro.nolleogasil_backend.dto.users.KakaoTokens;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;

/**
 * 이 인터페이스는 Feign Client를 이용한 카카오 로그인을 위한 Service Interface입니다.
 * @author 장민정
 * @since 2024-01-05
 */
@FeignClient(name = "kakaoClient", configuration = UsersConfig.class)
public interface KakaoClient {
    @PostMapping
    KakaoInfo getInfo(URI baseUrl, @RequestHeader("Authorization") String accessToken);

    @PostMapping
    KakaoTokens getToken(URI baseUrl, @RequestParam("client_id") String restApiKey,
                         @RequestParam("redirect_uri") String redirectUrl,
                         @RequestParam("code") String code,
                         @RequestParam("grant_type") String grantType);
}