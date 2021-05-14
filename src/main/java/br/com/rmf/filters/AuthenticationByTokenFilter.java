package br.com.rmf.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.rmf.entities.User;
import br.com.rmf.repositories.UserRepository;
import br.com.rmf.services.TokenService;

public class AuthenticationByTokenFilter extends OncePerRequestFilter {

	private TokenService tokenService;
	private UserRepository userRepository;

	public AuthenticationByTokenFilter(TokenService tokenService, UserRepository userRepository) {
		this.tokenService = tokenService;
		this.userRepository = userRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = recoverToken(request);
		boolean valid = tokenService.isTokenValid(token);
		if(valid) {
			authenticateClient(token);
		}
		filterChain.doFilter(request, response);
	}

	private void authenticateClient(String token) {
		Long userId = tokenService.getUserId(token);
		User user = userRepository.findById(userId).get();
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(token, null, user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	private String recoverToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
			return null;
		}
		return token.substring(7, token.length());
	}

}
