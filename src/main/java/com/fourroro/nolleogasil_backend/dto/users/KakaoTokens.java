package com.fourroro.nolleogasil_backend.dto.users;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 이 클래스는 카카오 로그인을 위한 Kakao Token DTO입니다.
 * @author 장민정
 * @since 2024-01-05
 */
/*Access Token을 받아오기 위한 Response Model*/
@ToString
@Getter
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoTokens {
    //사용자 액세스 토큰 값
    private String accessToken;

    //토큰 타입, bearer로 고정
    private String tokenType;

    //사용자 리프레시 토큰 값
    private String refreshToken;

    //액세스 토큰의 만료 시간(초)
    private String expiresIn;

    //리프레시 토큰 만료 시간(초)
    private String refreshTokenExpiresIn;

    public static KakaoTokens fail(){
        return new KakaoTokens(null, null);
    }

    private KakaoTokens(final String accessToken, final String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}