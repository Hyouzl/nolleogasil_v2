package com.fourroro.nolleogasil_backend.service.Oauth2;


import com.fourroro.nolleogasil_backend.entity.users.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Builder
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class OAuth2Attribute {

    private Map<String, Object> attributes; // 사용자 속성 정보를 담는 Map
    private String attributeKey; // 사용자 속성의 키
    private String name;
    private String email;
    private String nickname;
    private String phone;
    private String gender;
    private String provider; // 제공자 정보
    private String role;

    static OAuth2Attribute of(String provider, String attributeKey,
                              Map<String, Object> attributes) {
        switch (provider) {
            case "google":
                return ofGoogle(provider, attributeKey, attributes);
            case "kakao":
                return ofKakao(provider, attributeKey, attributes);
             case "naver":
                return ofNaver(provider, attributeKey, attributes);

            default:
                throw new RuntimeException();
        }

    }

    private static OAuth2Attribute ofGoogle(String provider,String attributeKey,
                                            Map<String, Object> attributes) {
        return OAuth2Attribute.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .attributes(attributes)
                .attributeKey(attributeKey)
                .provider(provider)
                .build();
    }

    private static OAuth2Attribute ofKakao(String provider, String attributeKey,
                                           Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuth2Attribute.builder()
                .name((String) kakaoAccount.get("name"))
                .email((String) kakaoAccount.get("email"))
                .nickname((String)kakaoProfile.get("nickname"))
                .gender((String)kakaoAccount.get("gender"))
                .phone((String)kakaoAccount.get("phone_number"))
                .attributes(kakaoAccount)
                .attributeKey(attributeKey)
                .provider(provider)
                .build();
    }


    private static OAuth2Attribute ofNaver(String provider, String attributeKey,
                                           Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuth2Attribute.builder()
                .name((String) response.get("name"))
                .nickname((String) response.get("nickname"))
                .email((String) response.get("email"))
                .phone((String) response.get("profile_image"))
                .attributes(response)
                .attributeKey(attributeKey)
                .provider(provider)
                .build();
    }


    // OAuth2User 객체에 넣어주기 위해서 Map으로 값들을 반환해준다.
     Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", attributeKey);
        map.put("email", email);
        map.put("nickname", nickname);
        map.put("gender", gender);
        map.put("phone", phone);
        map.put("provider", provider);
        map.put("name", name);

        return map;
    }

    public Users toUsers() {
        return Users.builder()
                .name(name)
                .email(email)
                .nickname(nickname)
                .phone(phone)
                .gender(gender)
                .provider(provider)
                .build();
    }


}
