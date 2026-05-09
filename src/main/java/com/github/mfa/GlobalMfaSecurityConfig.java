package com.github.mfa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authorization.EnableMultiFactorAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.FactorGrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;


//para toda a aplicacao
@Configuration
@EnableWebSecurity
@EnableMultiFactorAuthentication(
        authorities = { FactorGrantedAuthority.PASSWORD_AUTHORITY, FactorGrantedAuthority.OTT_AUTHORITY }
)
public class GlobalMfaSecurityConfig {

    @Bean
    @Order(3)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, ConsoleOttSender ottSender) throws Exception {
        http.securityMatcher("/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public", "/login", "/login/ott", "/ott/generate").permitAll()
                        .anyRequest().authenticated())
                .formLogin(withDefaults())
                .oneTimeTokenLogin(ott -> ott.tokenGenerationSuccessHandler(ottSender));

        return http.build();
    }
}