package br.com.rmf.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.rmf.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

	@Value("${account.jwt.expiration}")
	private String expiration;

	@Value("${account.jwt.secret}")
	private String secret;

	public String generateToken(Authentication auth) {

		User loged = (User) auth.getPrincipal();
		Date today = new Date();
		Date dtExpiration = new Date(today.getTime() + Long.parseLong(expiration));

		return Jwts.builder().setIssuer("Sankhya ID").setSubject(loged.getId().toString()).setIssuedAt(today)
				.setExpiration(dtExpiration).signWith(SignatureAlgorithm.HS256, secret).compact();
	}

	public boolean isTokenValid(String token) {
		try {
			Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public Long getUserId(String token) {
		Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
		return Long.parseLong(claims.getSubject());
	}

}
