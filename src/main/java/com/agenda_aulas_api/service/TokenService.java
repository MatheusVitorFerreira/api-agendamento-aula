package com.agenda_aulas_api.service;

import com.agenda_aulas_api.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenService {

	private final JwtEncoder jwtEncoder;

	private final long expirationTimeInMinutes = 120;

	public TokenResponse generateToken(User user) {
		Instant now = Instant.now();
		Instant expiresAt = now.plus(expirationTimeInMinutes, ChronoUnit.MINUTES);

		String scope = user.getRoles().stream()
				.map(r -> r.getName())
				.collect(Collectors.joining(" "));

		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer("agenda_aulas_api")
				.issuedAt(now)
				.expiresAt(expiresAt)
				.subject(user.getUsername())
				.claim("scope", scope)
				.build();

		String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

		return new TokenResponse(token, expiresAt.toString());
	}

	@Getter
	public static class TokenResponse {
		private final String token;
		private final String expiration;

		public TokenResponse(String token, String expiration) {
			this.token = token;
			this.expiration = expiration;
		}
	}
}
