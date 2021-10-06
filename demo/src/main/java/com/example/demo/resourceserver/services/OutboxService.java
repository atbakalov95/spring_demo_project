package com.example.demo.resourceserver.services;

import com.example.demo.resourceserver.enums.MessageStatusEnum;
import com.example.demo.resourceserver.exceptions.ServiceException;
import com.example.demo.resourceserver.model.Outbox;

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
