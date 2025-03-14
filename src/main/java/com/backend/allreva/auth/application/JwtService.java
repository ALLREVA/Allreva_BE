package com.backend.allreva.auth.application;

import com.backend.allreva.auth.domain.RefreshToken;
import com.backend.allreva.auth.domain.RefreshTokenRepository;
import com.backend.allreva.auth.exception.code.TokenInvalidException;
import com.backend.allreva.auth.exception.code.TokenNotFoundException;
import com.backend.allreva.auth.exception.code.TokenNotMatchException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class JwtService {

    private final SecretKey secretKey;
    private final int accessTime;
    private final int refreshTime;

    private final RefreshTokenRepository refreshTokenRepository;

    public JwtService(
            @Value("${jwt.secret-key}") final String secretKey,
            @Value("${jwt.access.expiration}") final int accessTime,
            @Value("${jwt.refresh.expiration}") final int refreshTime,
            final RefreshTokenRepository refreshTokenRepository
    ) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
        this.accessTime = accessTime;
        this.refreshTime = refreshTime;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * header에 있는 Access Token을 추출합니다.
     * @param request HTTP 요청
     * @return Access Token String 값
     */
    public String extractAccessToken(final HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * 토큰에서 memberId를 추출합니다.
     * @param token 토큰
     * @return memberId String 값
     */
    public String extractMemberId(final String token) {
        return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
    }

    /**
     * 토큰을 검증합니다.
     * @param token 토큰
     */
    public void validateToken(final String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parse(token);
        } catch (SignatureException e) {
            log.error("Invalid JWT token signature: {}", e.getMessage());
            throw new TokenInvalidException();
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            throw new TokenInvalidException();
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token: {}", e.getMessage());
            throw new TokenInvalidException();
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
            throw new TokenInvalidException();
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
            throw new TokenInvalidException();
        }
    }

    /**
     * Access Token을 생성합니다.
     * @param subject 토큰에 담을 subject 값
     * @return Access Token String 값
     */
    public String generateAccessToken(final String subject) {
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + accessTime);

        return Jwts.builder()
                .subject(subject)
                .issuedAt(currentDate)
                .expiration(expireDate)
                .signWith(secretKey)
                .compact();
    }

    /**
     * Refresh Token을 생성합니다.
     * @param subject 토큰에 담을 subject 값
     * @return Refresh Token String 값
     */
    public String generateRefreshToken(final String subject) {
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + refreshTime);

        return Jwts.builder()
                .subject(subject)
                .issuedAt(currentDate)
                .expiration(expireDate)
                .signWith(secretKey)
                .compact();
    }

    /**
     * Refresh Token을 Redis에 새로 갱신합니다.
     * @param generatedRefreshToken 새로 생성된 Refresh Token
     * @param memberId 회원 ID
     */
    public void updateRefreshToken(
            final String generatedRefreshToken,
            final Long memberId
    ) {
        refreshTokenRepository.findRefreshTokenByMemberId(memberId)
                .ifPresent(refreshTokenRepository::delete);

        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .token(generatedRefreshToken)
                .memberId(memberId)
                .build();
        refreshTokenRepository.save(refreshTokenEntity);
    }

    /**
     * Refresh Token이 Redis에 존재하는지 확인합니다.
     * @param refreshToken Cookie에 저장되있던 Refresh Token
     */
    public void validRefreshTokenExistInRedis(final String refreshToken) {
        Optional<RefreshToken> refreshTokenFromRedis = refreshTokenRepository.findRefreshTokenByToken(refreshToken);
        if (refreshTokenFromRedis.isEmpty()) {
            throw new TokenNotFoundException();
        }
        if (!refreshTokenFromRedis.get().getToken().equals(refreshToken)) {
            throw new TokenNotMatchException();
        }
    }

    /**
     * Refresh Token을 Redis에서 삭제합니다.
     * @param refreshToken Cookie에 저장되있던 Refresh Token
     */
    public void deleteRefreshTokenInRedis(final String refreshToken) {
        Optional<RefreshToken> refreshTokenFromRedis = refreshTokenRepository.findRefreshTokenByToken(refreshToken);
        refreshTokenFromRedis.ifPresent(refreshTokenRepository::delete);
    }
}
