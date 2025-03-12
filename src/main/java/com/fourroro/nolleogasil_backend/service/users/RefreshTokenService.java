package com.fourroro.nolleogasil_backend.service.users;

public interface RefreshTokenService {
    void deleteByUserId(Long userId);
    void saveToken(Long userId, String refreshToken);
}
