package com.fourroro.nolleogasil_backend.service.users;

public interface RefreshTokenService {
    void deleteByEmail(String userId);
    void saveToken(String userId, String refreshToken);
}
