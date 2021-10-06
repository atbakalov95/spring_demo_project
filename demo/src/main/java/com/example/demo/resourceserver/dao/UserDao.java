package com.example.demo.resourceserver.dao;

import com.example.demo.resourceserver.model.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends AbstractHibernateDao<User> {
    public UserDao() {
        super(User.class);
    }
}
