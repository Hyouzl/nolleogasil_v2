package com.fourroro.nolleogasil_backend.dto.users;

import lombok.*;

/**
 * 이 클래스는 카카오 로그인을 위한 token DTO입니다.
 * @author 장민정
 * @since 2024-01-05
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TokenRequest {
    private String grant_type;
    private String client_id;
    private String redirect_uri;
    private String client_secret;
    private String code;
}

