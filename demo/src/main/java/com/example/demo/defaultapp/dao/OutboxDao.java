package com.example.demo.defaultapp.dao;

import com.example.demo.defaultapp.enums.MessageStatusEnum;
import com.example.demo.defaultapp.model.Outbox;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OutboxDao extends AbstractHibernateDao<Outbox> {
    public OutboxDao() {super(Outbox.class);}

    public List<Outbox> findTopNWithStatus(int limit, MessageStatusEnum messageStatusEnum) {
        return getCurrentSession().createQuery("SELECT o FROM Outbox o WHERE o.status = :status", Outbox.class)
                .setParameter("status", messageStatusEnum)
                .setMaxResults(limit)
                .setFirstResult(0)
                .getResultList();
    }

    public Outbox findByMessage(String message) {
        return getCurrentSession().createQuery("SELECT o FROM Outbox o WHERE o.message = :message", Outbox.class)
                .setParameter("message", message)
                .getSingleResult();
    }
}
