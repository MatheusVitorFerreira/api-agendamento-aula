package com.agenda_aulas_api.service;

import com.agenda_aulas_api.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtEncoder jwtEncoder;
    private final long expirationTimeInMinutes = 120;

    public TokenResponse generateToken(User user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(expirationTimeInMinutes, ChronoUnit.MINUTES);


        String scope = "SCOPE_" + user.getUserType().name();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("agenda_aulas_api")
                .subject(user.getUsername())
                .issuedAt(now)
                .expiresAt(expiresAt)
                .claim("scope", scope)
                .claim("userId", user.getUserId().toString())
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new TokenResponse(token, expiresAt.toEpochMilli());
    }

    @Getter
    public static class TokenResponse {
        private final String token;
        private final Long expiration;

        public TokenResponse(String token, Long expiration) {
            this.token = token;
            this.expiration = expiration;
        }
    }
}
