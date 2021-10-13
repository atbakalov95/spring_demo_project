package com.example.demo.defaultapp.services;

import com.example.demo.defaultapp.enums.MessageStatusEnum;
import com.example.demo.defaultapp.exceptions.ServiceException;
import com.example.demo.defaultapp.model.Outbox;

import java.util.List;
import java.util.UUID;

public interface OutboxService {
    void persist(Object object) throws ServiceException;
    void update(Outbox outbox);
    public UUID save(Object object) throws ServiceException;
    Outbox findOne(UUID uuid);
    List<Outbox> findAll();
    List<Outbox> findTopNWithStatus(int limit, MessageStatusEnum messageStatusEnum);
    Outbox findByMessage(String message);
}
