package com.naveen.stayease.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

@Configuration
public class SecurityConfig {

    private final int PASSWORD_ENCODING_STRENGTH = 12;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrfConfig -> csrfConfig.disable())
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/user/register**", "/login", "/error").permitAll()
                        .requestMatchers("/account", "/book","/hotel/*").authenticated());
        http.formLogin(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(PASSWORD_ENCODING_STRENGTH);
    }

    /**
     * From Spring Security 6.3 version
     * The CompromisedPasswordChecker is a component in Spring Security that checks if a password has been compromised in a data breach.
     * @return compromised password checker
     */
    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }
}
