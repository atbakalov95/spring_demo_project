package com.example.demo.authserver.users;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public class OAuthDao {
    private final JdbcTemplate jdbcTemplate;
    @Value("${roles.admin}")
    private String adminRole;

    public OAuthDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public UserEntity getUserDetails(String username) {
        Collection<GrantedAuthority> grantedAuthoritiesList = new ArrayList<>();
        String userSQLQuery = "SELECT * FROM USERS WHERE USERNAME=?";
        List<UserEntity> list = this.jdbcTemplate.query(userSQLQuery, new String[]{username},
                (ResultSet rs, int rowNum) -> {
                    UserEntity user = new UserEntity();
                    user.setUsername(username);
                    user.setPassword(rs.getString("PASSWORD"));
                    return user;
                }
        );
        if (list.size() > 0) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(this.adminRole);
            grantedAuthoritiesList.add(grantedAuthority);
            list.get(0).setGrantedAuthoritiesList(grantedAuthoritiesList);
            return list.get(0);
        }
        return null;
    }
}
