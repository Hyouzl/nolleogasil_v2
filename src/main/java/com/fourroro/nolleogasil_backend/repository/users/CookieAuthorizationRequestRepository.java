package com.fourroro.nolleogasil_backend.repository.users;

import com.fourroro.nolleogasil_backend.service.Oauth2.CookieUtils;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Repository;

//Provider와의 Authorization 과정에서 Authorization request를 cookie에 저장하기 위한 클래스
//oauth2_auth_request 쿠키 : 해당 Authorizaion request의 고유 아이디를 담는다
//redirect_uri 쿠키 : 해당 Authorization request시 파라미터로 넘어온 redirect_uri를 담는다. 이 쿠키는 나중에 applcation.yml의 authorizedRedirectUri와 일치하는지 확인시 사용된다
//http://localhost:8080/oauth2/authorize/google?redirect_uri=http://localhost:3000/oauth2/redirect
//인증 요청시 생성된 2개의 쿠키는 인증이 종료될 때 실행되는 successHandler와 failureHandler에서 제거된다
//2개의 쿠키 유효시간은 180초로 유효시간 내에 인증요청을 다시하면 만들어졌던 쿠키를 다시 사용한다



@Repository
public class CookieAuthorizationRequestRepository implements AuthorizationRequestRepository {
    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";

    private static final int cookieExpireSeconds = 60*60;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) { return CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
            .map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
            .orElse(null);
    }


    //인가 요청 담기.
    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
            CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
            return;
        }

        CookieUtils.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, CookieUtils.serialize(authorizationRequest), cookieExpireSeconds);
        String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
        if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
            CookieUtils.addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, cookieExpireSeconds);
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        return this.loadAuthorizationRequest(request);
    }


    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
    }
}


