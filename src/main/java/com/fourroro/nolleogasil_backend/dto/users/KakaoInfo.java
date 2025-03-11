package com.fourroro.nolleogasil_backend.dto.users;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 이 클래스는 카카오 로그인을 위한 DTO입니다.
 * @author 장민정
 * @since 2024-01-05
 */
@ToString
@Getter
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoInfo {
    private KakaoDto kakaoDto;

    public static KakaoInfo fail(){
        return null;
    }
}
