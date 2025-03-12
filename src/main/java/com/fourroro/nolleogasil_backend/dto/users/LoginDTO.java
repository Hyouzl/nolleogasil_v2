package com.fourroro.nolleogasil_backend.dto.users;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class LoginDTO {

    @Getter
    @Builder
    public static class RequestLoginDTO {
        private String loginId;
        private String password;
    }

    @Getter
    @Setter
    @Builder
    public static class RequestRegisterDTO {
        private String loginId;
        private String password;
        private String name;
        private String email;
        private String nickname;
        private String phone;
        private String gender;
    }
}
