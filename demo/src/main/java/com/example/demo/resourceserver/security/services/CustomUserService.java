package com.example.demo.resourceserver.security.services;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

public class CustomUserService extends JdbcDaoImpl {
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

    public CustomUserService(JdbcTemplate jdbcTemplate) {
        super();

        this.setJdbcTemplate(jdbcTemplate);
        this.setEnableAuthorities(true);
        this.setUsersByUsernameQuery(SELECT_USER_BY_USERNAME_QUERY);
        this.setAuthoritiesByUsernameQuery(SELECT_AUTHORITIES_BY_USERNAME_QUERY);
    }


}
