package com.fourroro.nolleogasil_backend.auth.jwt;


import lombok.Builder;
import lombok.Getter;

import java.util.Date;


@Getter
@Builder
public class JwtTokenResponseDto {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpires;
    private Date accessTokenExpiresDate;
}
