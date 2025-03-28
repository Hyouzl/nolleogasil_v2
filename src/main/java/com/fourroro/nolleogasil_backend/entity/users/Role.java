package com.fourroro.nolleogasil_backend.entity.users;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    ROLE_ADMIN("ROLE_ADMIN", "관리자"),
    ROLE_GUEST("ROLE_GUEST" , "손님"),
    ROLE_USER("ROLE_USER" , "일반 사용자");

    private final String key;
    private final String title;

}
