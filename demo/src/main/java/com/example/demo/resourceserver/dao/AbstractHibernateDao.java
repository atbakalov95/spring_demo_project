package com.example.demo.resourceserver.dao;

import org.hibernate.Session;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

public abstract class AbstractHibernateDao<T> {
    private final Class<T> clazz;
    @PersistenceContext
    protected EntityManager entityManager;

    public AbstractHibernateDao(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T findOne(Serializable id) {
        return (T) getCurrentSession().get( clazz, id );
    }
    public List<T> findAll() {
        return getCurrentSession()
                .createQuery("from " + clazz.getName(), clazz)
                .list();
    }

    public void persist(T entity) {
        getCurrentSession().persist(entity);
    }

    public T update(T entity) {
        return (T) getCurrentSession().merge(entity);
    }

    public void delete(T entity) {
        getCurrentSession().delete(entity);
    }
    public void deleteById(Serializable id) {
        final T entity = findOne( id);
        delete( entity );
    }

    protected final Session getCurrentSession(){
        try{
            return entityManager.unwrap(Session.class);
        } catch (Exception e) {
            throw new NullPointerException();
        }
    }
}

