package com.eventpass.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;
import java.util.List;

@Configuration
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtFilter;
    private final String frontendUrl;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter, @Value("${app.frontend-url}") String frontendUrl) {
        this.jwtFilter = jwtFilter;
        this.frontendUrl = frontendUrl;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/events", "/api/events/{eventId}", "/api/events/{eventId}/timeslots").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/events/my", "/api/events/{eventId}/registrations").hasAnyRole("ORGANIZER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/events").hasAnyRole("ORGANIZER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/events/{eventId}/open", "/api/events/{eventId}/close").hasAnyRole("ORGANIZER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/events/{eventId}").hasAnyRole("ORGANIZER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/events/{eventId}/timeslots").hasAnyRole("ORGANIZER", "ADMIN")
                .requestMatchers("/api/registrations/**").authenticated()
                .requestMatchers("/api/passes/**").authenticated()
                .requestMatchers("/api/timeslots/**").authenticated()
                .requestMatchers("/api/users/**").authenticated()
                .anyRequest().authenticated())
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(frontendUrl));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
