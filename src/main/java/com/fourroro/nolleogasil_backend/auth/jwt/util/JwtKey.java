package com.fourroro.nolleogasil_backend.auth.jwt.util;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
@Getter
@Component
@ConfigurationProperties(prefix = "jwt") // ✅ "jwt" 네임스페이스 아래 모든 설정을 읽음
public class JwtKey {

        private String securityKey;

        public void setSecurityKey(String securityKey) {
            System.out.println("✅ Loaded JWT Secret Key: " + securityKey); // ✅ 정상적으로 로드되는지 확인
            this.securityKey = securityKey;
        }

}
