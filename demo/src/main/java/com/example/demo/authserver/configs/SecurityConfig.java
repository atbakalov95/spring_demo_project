package com.example.demo.authserver.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String SELECT_AUTHORITIES_BY_USERNAME_QUERY =
            "WITH user_and_role AS (SELECT name, role_id FROM users WHERE name=?) " +
            "SELECT auth.name AS authority, (SELECT name FROM user_and_role) AS username FROM " +
                "(SELECT authority_id FROM roles_to_authorities WHERE role_id=" +
                    "(SELECT role_id FROM user_and_role)" +
                ") as map" +
                    " JOIN " +
                "authorities AS auth " +
                    "ON map.authority_id=auth.id;";
    private static final String SELECT_USER_BY_USERNAME_QUERY =
            "SELECT name AS username, password, true AS enabled FROM users WHERE name = ?";
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        JdbcDaoImpl userDetails = new JdbcDaoImpl();
        userDetails.setJdbcTemplate(jdbcTemplate);
        userDetails.setEnableAuthorities(true);
        userDetails.setUsersByUsernameQuery(SELECT_USER_BY_USERNAME_QUERY);
        userDetails.setAuthoritiesByUsernameQuery(SELECT_AUTHORITIES_BY_USERNAME_QUERY);
        return userDetails;
    }
}
