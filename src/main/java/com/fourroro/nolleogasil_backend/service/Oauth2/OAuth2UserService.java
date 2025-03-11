package com.fourroro.nolleogasil_backend.service.Oauth2;

import com.fourroro.nolleogasil_backend.entity.users.PrincipalDetails;
import com.fourroro.nolleogasil_backend.entity.users.Role;
import com.fourroro.nolleogasil_backend.entity.users.Users;
import com.fourroro.nolleogasil_backend.repository.users.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;


// OAuth2 제공자(예: Google, Naver, Kakao)로부터 받아온 사용자 정보를 이용해
// 애플리케이션의 사용자 데이터를 생성하거나 갱신하는 역할을 합니다.
@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UsersRepository usersRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // OAuth2 인증된 사용자의 정보를 담고 있는 OAuth2User 객체를 가져온다.
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 클라이언트 등록 ID(google, naver, kakao)와 사용자 이름 속성을 가져온다.
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // OAuth2제공자가 사용자 정보를 제공할 때 사용하는 사용자 이름 속성을 가져온다. ex) 구글(sub)
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // OAuth2UserService를 사용하여 가져온 OAuth2User 정보로 OAuth2Attribute 객체를 만든다.
        OAuth2Attribute oAuth2Attribute =
                OAuth2Attribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Map<String, Object> usersAttribute = oAuth2Attribute.convertToMap();
        String subject;
        if (usersAttribute.get("provider").equals("kakao")) {
            subject = String.valueOf(oAuth2User.getAttributes().get(userNameAttributeName));
        } else {
            subject = (String) oAuth2User.getAttributes().get(userNameAttributeName);

        }

        Users user = saveOrUpdate(oAuth2Attribute);

        // 회원의 권한과, 회원속성, 속성이름을 이용해 DefaultOAuth2User 객체를 생성해 반환한다.
        return new PrincipalDetails(user, usersAttribute, subject);
    }

    /**
     *
     * email과 provider 조합으로 사용자를 식별해야만 각 소셜 로그인 제공자별로
     * 동일한 이메일을 사용하는 여러 사용자를 구분할 수 있습니다.
     *
     * **/
    private Users saveOrUpdate(OAuth2Attribute oAuth2Attribute) {

        Optional<Users> existingUser = usersRepository.findByEmail(oAuth2Attribute.getEmail());

        // 이미 가입된 이메일이 다른 소셜 제공자로 사용된 경우
        if (existingUser.isPresent() && !existingUser.get().getProvider().equals(oAuth2Attribute.getProvider())) {
            throw new RuntimeException("해당 이메일은 이미 " + existingUser.get().getProvider() + "로 회원가입되어 있습니다. 기존 로그인 방식을 사용해주세요.");
        }

        // 다른 추가 정보로 동일 사용자인지 확인
        Optional<Users> existingUserByName = usersRepository.findByName(oAuth2Attribute.getName());
        if (existingUserByName.isPresent()) {
            // 동일한 전화번호를 가진 사용자가 이미 존재할 경우 중복 가입 제한
            return existingUserByName.get();
        }
        Users users = usersRepository.findByEmailAndProvider(
                oAuth2Attribute.getEmail(), oAuth2Attribute.getProvider())
                .map(user -> user.update(oAuth2Attribute.getName(), oAuth2Attribute.getEmail()))
                .orElse(oAuth2Attribute.toUsers());

        if (users.getRole() == null) {
            users.setRole(Role.ROLE_USER); // 기본 역할 설정
        }

        return usersRepository.save(users);
    }


}

