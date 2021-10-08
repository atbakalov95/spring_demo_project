package com.example.demo.authserver.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

@Configuration
public class UserDetailsConfig {
    private final JdbcTemplate jdbcTemplate;

    public UserDetailsConfig(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String SELECT_AUTHORITIES_BY_USERNAME_QUERY =
            "WITH user_and_role AS (SELECT name, role_id FROM users WHERE name=?) " +
                    "SELECT (SELECT name FROM user_and_role) AS username, auth.name AS authority FROM " +
                    "(SELECT authority_id FROM roles_to_authorities WHERE role_id=" +
                    "(SELECT role_id FROM user_and_role)" +
                    ") as map" +
                    " JOIN " +
                    "authorities AS auth " +
                    "ON map.authority_id=auth.id;";
    private static final String SELECT_USER_BY_USERNAME_QUERY =
            "SELECT name AS username, password, true AS enabled FROM users WHERE name = ?";

    @Bean
    @Primary
    protected UserDetailsService userDetailsService() {
        JdbcDaoImpl userDetails = new JdbcDaoImpl();
        userDetails.setJdbcTemplate(jdbcTemplate);
        userDetails.setEnableAuthorities(true);
        userDetails.setUsersByUsernameQuery(SELECT_USER_BY_USERNAME_QUERY);
        userDetails.setAuthoritiesByUsernameQuery(SELECT_AUTHORITIES_BY_USERNAME_QUERY);
        return userDetails;
    }
}
