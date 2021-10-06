package com.example.demo.resourceserver.dao;

import com.example.demo.resourceserver.model.Animal;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AnimalDao extends AbstractHibernateDao<Animal> {
    public AnimalDao() {
        super(Animal.class);
    }

    public List<Animal> findByOwner(String owner) {
        return getCurrentSession()
                .createQuery("select a from Animal a where a.owner=:owner", Animal.class)
                .setParameter("owner", owner)
                .list();
    }

    public List<Animal> findByName(String name) {
        return getCurrentSession()
                .createQuery("select a from Animal a where a.name=:name", Animal.class)
                .setParameter("name", name)
                .list();
    }
}
