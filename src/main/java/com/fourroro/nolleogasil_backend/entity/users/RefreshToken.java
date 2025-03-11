package com.fourroro.nolleogasil_backend.entity.users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id
    private String email;

    @Column(nullable = false)
    private String token;

    public void updateToken(String token) {
        this.token = token;
    }
}
