package com.example.demo.resourceserver.configs;

import com.example.demo.resourceserver.security.services.CustomOAuth2UserService;
import com.example.demo.resourceserver.security.services.CustomUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Configuration
public class UserDetailsConfig {
    private final JdbcTemplate jdbcTemplate;

    public UserDetailsConfig(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Bean
    @Primary
    UserDetailsService userDetailsService() {
        return new CustomUserService(jdbcTemplate);
    }

    @Bean
    @Primary
    OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService(UserDetailsService userDetailsService) {
        return new CustomOAuth2UserService(userDetailsService);
    }
}
