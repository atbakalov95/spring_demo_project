package com.example.demo.resourceserver.dao;

import com.example.demo.resourceserver.model.Animal;
import org.springframework.stereotype.Repository;

@Repository
public class AnimalDao extends AbstractHibernateDao<Animal> {
    public AnimalDao() {
        super(Animal.class);
    }
}
