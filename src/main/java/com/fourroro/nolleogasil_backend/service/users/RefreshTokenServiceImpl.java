package com.fourroro.nolleogasil_backend.service.users;

import com.fourroro.nolleogasil_backend.entity.users.RefreshToken;
import com.fourroro.nolleogasil_backend.repository.users.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService{

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void deleteByEmail(String userId) {
        refreshTokenRepository.deleteByEmail(userId);
    }

    @Override
    public void saveToken(String email, String refreshToken) {
        refreshTokenRepository.findByEmail(email)
                .ifPresentOrElse(
                        existingToken -> existingToken.updateToken(refreshToken),
                        () -> refreshTokenRepository.save(RefreshToken.builder()
                                .email(email)
                                .token(refreshToken)
                                .build())
                );

    }
}
