package com.github.mfa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authorization.AuthorizationManagerFactories;
import org.springframework.security.authorization.AuthorizationManagerFactory;
import org.springframework.security.authorization.AuthorizationManagers;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.time.Duration;

@Configuration
@EnableWebSecurity
public class TimeBasedMfaSecurityConfig {

    @Bean
    @Order(2)
    SecurityFilterChain profileSecurityFilterChain(HttpSecurity http) {
        AuthorizationManagerFactory<Object> recentLogin = AuthorizationManagerFactories
                .multiFactor()
                .requireFactor(factor -> factor.passwordAuthority()
                        .validDuration(Duration.ofMinutes(5)))
                .build();

        http.securityMatcher("/profile", "/profile/**").authorizeHttpRequests(
                auth -> auth.requestMatchers("/profile", "/profile/**")
                        .access(recentLogin.authenticated())
                        .anyRequest()
                        .authenticated()).formLogin(Customizer.withDefaults());

        return http.build();
    }
}
