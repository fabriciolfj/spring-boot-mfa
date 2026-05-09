package com.github.mfa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authorization.AuthorizationManagerFactories;
import org.springframework.security.authorization.AuthorizationManagerFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.FactorGrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;


//por endpoint
@Configuration
@EnableWebSecurity
public class AdminMfaSecurityConfig {

    @Bean
    @Order(1)
    SecurityFilterChain adminSecurityFilterChain(HttpSecurity http,  ConsoleOttSender ottSender) throws Exception {
        AuthorizationManagerFactory<Object> mfa = AuthorizationManagerFactories.multiFactor()
                .requireFactors(
                        FactorGrantedAuthority.PASSWORD_AUTHORITY,
                        FactorGrantedAuthority.OTT_AUTHORITY)
                .build();

        http.securityMatcher("/admin/**")
                .authorizeHttpRequests(auth ->auth.requestMatchers("/admin/**")
                .access(mfa.hasRole("ADMIN")).anyRequest().authenticated())
                .formLogin(withDefaults())
                .oneTimeTokenLogin(ott -> ott.tokenGenerationSuccessHandler(ottSender));

        return http.build();
    }
}