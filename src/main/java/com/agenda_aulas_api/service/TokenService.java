package com.agenda_aulas_api.service;


import com.agenda_aulas_api.domain.User;
import com.agenda_aulas_api.repository.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Data;
import lombok.Getter;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@Data
public class TokenService {

	private final String secret = "";
	private final long expirationTimeInMinutes = 120;
	private UserRepository userRepository;


	public TokenResponse generateToken(User user) {
		Instant expirationInstant = Instant.now().plus(expirationTimeInMinutes, ChronoUnit.MINUTES);
		Date expirationDate = Date.from(expirationInstant);

		return generateToken(user, expirationDate);
	}

	public TokenResponse generateToken(User user, Date expirationDate) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);

			String token = JWT.create().withIssuer("auth").withSubject(user.getUsername()).withExpiresAt(expirationDate)
					.sign(algorithm);

			Instant expirationInstant = expirationDate.toInstant();
			return new TokenResponse(token, expirationInstant.toString());

		} catch (JWTVerificationException exception) {
			throw new RuntimeException("Erro ao gerar token", exception);
		}
	}

	public String validateToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			DecodedJWT decodedJWT = JWT.require(algorithm).build().verify(token);
			return decodedJWT.getSubject();
		} catch (JWTVerificationException exception) {
			return null;
		}
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
