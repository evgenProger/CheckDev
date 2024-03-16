package ru.checkdev.generator.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@Configuration
@EnableWebSecurity
@EnableResourceServer
public class SecurityConfig {

    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET,
                        "/exam/")
                    .permitAll()
                .anyRequest().authenticated()
            .and()
            .csrf()
                .disable()
                .headers()
                .xssProtection()
                .block(true)
                .xssProtectionEnabled(true);
    }
}