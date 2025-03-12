package com.fourroro.nolleogasil_backend.service.users;

import com.fourroro.nolleogasil_backend.auth.jwt.JwtTokenResponseDto;
import com.fourroro.nolleogasil_backend.auth.jwt.util.TokenProvider;
import com.fourroro.nolleogasil_backend.dto.users.LoginDTO;
import com.fourroro.nolleogasil_backend.dto.users.TokenDTO;
import com.fourroro.nolleogasil_backend.entity.users.PrincipalDetails;
import com.fourroro.nolleogasil_backend.entity.users.RefreshToken;
import com.fourroro.nolleogasil_backend.repository.users.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    @Override
    public TokenDTO.ResponseTokenDTO login(LoginDTO.RequestLoginDTO loginRequestDto) {
        String loginId = loginRequestDto.getLoginId();
        String password = loginRequestDto.getPassword();

        // 1. Login Id/PW 를 기반으로 Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginId, password);

        // 2. 실제 검증 (사용자 비밀번호 체크)
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 생성
        JwtTokenResponseDto tokenDto = tokenProvider.generateTokenDto(authentication, "COMMON");

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .userId(principalDetails.getUserId())
                .token(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        return TokenDTO.ResponseTokenDTO.builder()
                .accessToken(tokenDto.getAccessToken())
                .userId(refreshToken.getUserId())
                .nickname(principalDetails.getNickname())
                .build();
    }
}
