package com.fourroro.nolleogasil_backend.repository.users;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RedisOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisOAuth2AuthorizationRequestRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return (OAuth2AuthorizationRequest) redisTemplate.opsForValue().get(getKey(request));
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        redisTemplate.opsForValue().set(getKey(request), authorizationRequest, Duration.ofMinutes(10));
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        return (OAuth2AuthorizationRequest) redisTemplate.opsForValue().getAndDelete(getKey(request));
    }


    private String getKey(HttpServletRequest request) {
        return "OAUTH2_AUTH_REQUEST_" + request.getSession().getId();
    }
}


