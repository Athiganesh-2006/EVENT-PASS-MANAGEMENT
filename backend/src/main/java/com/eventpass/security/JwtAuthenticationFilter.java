package com.eventpass.security;

import com.eventpass.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

// Runs before controllers.
// It reads Authorization: Bearer <token>, verifies JWT, then puts the user/role into Spring Security.
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            try {
                Claims claims = jwtService.parseToken(token);
                Long userId = Long.valueOf(claims.getSubject());
                String email = String.valueOf(claims.get("email"));
                User.Role role = User.Role.valueOf(String.valueOf(claims.get("role")));

                // ROLE_ prefix is required by hasRole("ADMIN") etc.
                var authority = new SimpleGrantedAuthority("ROLE_" + role.name());

                var authentication = new UsernamePasswordAuthenticationToken(
                        userId, null, List.of(authority));

                // Store the authenticated user in Spring Security's context.
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (JwtException | IllegalArgumentException ex) {
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}
