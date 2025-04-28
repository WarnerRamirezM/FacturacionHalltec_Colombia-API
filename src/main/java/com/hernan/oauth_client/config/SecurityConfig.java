package com.hernan.oauth_client.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/get-token").permitAll() // Permitir sin login
                        .anyRequest().authenticated()              // Todo lo demás necesita login
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
