package com.fourroro.nolleogasil_backend.dto.users;

import lombok.Builder;
import lombok.Getter;

public class TokenDTO {

    @Getter
    @Builder
    public static class ResponseTokenDTO {
        private String accessToken;
        private Long userId;
        private String nickname;
    }

}
