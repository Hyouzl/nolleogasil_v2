package com.fourroro.nolleogasil_backend.auth.jwt.util;

import com.fourroro.nolleogasil_backend.apiPayLoad.Exception.InvalidJWTException;
import com.fourroro.nolleogasil_backend.auth.jwt.JwtTokenResponseDto;
import com.fourroro.nolleogasil_backend.entity.users.PrincipalDetails;
import com.fourroro.nolleogasil_backend.entity.users.Role;
import com.fourroro.nolleogasil_backend.entity.users.Users;
import com.fourroro.nolleogasil_backend.repository.users.RefreshTokenRepository;
import com.fourroro.nolleogasil_backend.service.Oauth2.OAuth2Attribute;
import com.fourroro.nolleogasil_backend.service.users.CustomUserDetailService;
import com.fourroro.nolleogasil_backend.service.users.RefreshTokenService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 7일
    @Autowired
    private CustomUserDetailService userDetailsService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    private Key key;
    @Autowired
    // application.yml에서 주입받은 secret 값을 base64 decode하여 key 변수에 할당
    public TokenProvider(JwtKey jwtProperties, RefreshTokenService refreshTokenService) {
        String secret = jwtProperties.getSecurityKey();
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public Authentication getAuthenticationFromUserId(String userId) {
        // userId를 기반으로 PrincipalDetails 가져오기
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public JwtTokenResponseDto generateTokenDto(Authentication authentication, String loginType) {

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        long now = (new Date()).getTime();
        Date accessExprTime = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshExprTime = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);
        String email = principalDetails.getUsername();
        String accessToken = "";
        String refreshToken = "";


        // accessToken 생성 (소셜 로그인 시 attributes 포함)
        JwtBuilder builder = Jwts.builder()
                .setSubject(String.valueOf(principalDetails.getUserId()))
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(accessExprTime);

        // 소셜 로그인인 경우 attributes 추가
        if (loginType.equals("SOCIAL")) {
            builder.claim("attributes", principalDetails.getAttributes());
        }

        accessToken = builder.compact();

        refreshToken = Jwts.builder()
                .setSubject(String.valueOf(principalDetails.getUserId()))
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(refreshExprTime)
                .compact();

        refreshTokenService.saveToken(String.valueOf(principalDetails.getUserId()), refreshToken);




        return JwtTokenResponseDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpires(ACCESS_TOKEN_EXPIRE_TIME - 5000) // 자동 로그아웃 기능을 위한 간극
                .accessTokenExpiresDate(accessExprTime)
                .build();

    }

    // 서명 검증 : JWT 토큰이 유효한지와 변조되지 않았는지 확인
    // 무결성과 발신자의 신뢰성을 보장
    //  Jwts.parserBuilder():
    //      JJWT 라이브러리의 Jwts 클래스는 JWT 토큰을 생성하거나 검증하기 위한 도구.
    //      parserBuilder()를 사용해 JWT 파서를 생성하기 위한 빌더를 시작한다.
    //  setSigningKey(key):
    //      JWT의 서명을 검증하기 위해 서명에 사용된 비밀키 또는 공개키를 설정.
    //      이 **키(key)**는 JWT 토큰을 생성할 때 사용된 것과 동일한 키여야 한다.
    //      이 키를 통해 JWT가 서명된 방식과 일치하는지, 즉 토큰이 발급된 이후 변조되지 않았는지를 확인.
    //  build():
    //      빌더 패턴을 통해 JWT 파서를 생성합니다. 이제 이 파서를 사용하여 JWT의 서명 검증을 수행할 수 있게 됨.
    //  parseClaimsJws(token):
    //      전달받은 JWT 토큰(token)을 파싱하여 서명 검증을 수행한다.
    //      이 과정에서 토큰의 Header, Payload, Signature를 분리하고, 서명을 검증하여 유효성을 확인한다.
    //      서명이 올바르지 않거나, 서명에 사용된 키가 잘못되었거나, 토큰이 변조되었다면 예외가 발생한다 (SignatureException, ExpiredJwtException 등).
    //  .getBody():
    //      서명 검증이 성공한 경우, **토큰의 페이로드(Payload)**에서 클레임(Claims) 정보를 추출한다.
    //      Claims는 JWT의 페이로드에 있는 정보를 담고 있으며, 사용자 ID, 권한, 만료 시간 등과 같은 정보를 포함할 수 있다.
    //  서명 검증이 성공한 이후, 토큰의 클레임에서 **권한 정보(AUTHORITIES_KEY)**를 확인합니다.
    //  만약 권한 정보가 없으면, 예외를 발생시켜 해당 JWT가 유효하지 않음을 나타냅니다.

    // JWT토큰을 복호화하여 토큰에 들어있는 정보를 꺼냅니다.
    public Authentication getAuthentication(String token) {

        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        if(claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // 사용자 정보 가져오기
        Long userId = Long.valueOf(claims.getSubject());
        String roleKey = claims.get(AUTHORITIES_KEY).toString();

        // Users 객체 생성
        Users user = Users.builder()
                .usersId(userId) // 사용자 ID 설정
                .role(Role.valueOf(roleKey)) // 역할 필드 설정
                .build();

        PrincipalDetails principal;
        // 토큰에서 OAuth2 사용자인지 여부를 판단할 수 있는 속성이 있는 경우 분기
        if (claims.get("provider") != null) {
            // OAuth2 로그인 처리
            Map<String, Object> attributes = claims.get("attributes", Map.class);
            principal = new PrincipalDetails(user, attributes, claims.getSubject());

        } else {
            // 일반 로그인 처리
            principal = new PrincipalDetails(user);
        }

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (JwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            throw new RuntimeException("Invalid JWT token");
        }
    }

    public void validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서면입니다.");
            throw new InvalidJWTException("잘못된 JWT 서면입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            throw new InvalidJWTException("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
            throw new InvalidJWTException("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
            throw new InvalidJWTException("JWT 토큰이 잘못되었습니다.");
        }
    }


    /**
     * JWT에서 만료 시간을 가져오는 메서드
     */
    public Date getExpiration(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration(); // JWT의 exp(만료 시간) 반환
    }

    /**
     * JWT의 남은 유효 시간(밀리초 단위) 반환
     */
    public long getExpirationTime(String token) {
        return getExpiration(token).getTime() - System.currentTimeMillis();
    }

    /**
     * JWT가 만료되었는지 확인
     */
    public boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }


}
