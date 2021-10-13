package com.example.demo.defaultapp.services.implementation;

import com.example.demo.defaultapp.dao.ZooDao;
import com.example.demo.defaultapp.exceptions.ServiceException;
import com.example.demo.defaultapp.model.Animal;
import com.example.demo.defaultapp.model.Zoo;
import com.example.demo.defaultapp.services.OutboxService;
import com.example.demo.defaultapp.services.ZooService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ZooServiceImpl implements ZooService {
    private static final Logger logger = Logger.getLogger(ZooServiceImpl.class.getName());

    private final ZooDao zooDao;
    private final OutboxService outboxService;

    public ZooServiceImpl(ZooDao zooDao, OutboxService outboxService) {
        this.zooDao = zooDao;
        this.outboxService = outboxService;
    }

    @Transactional(rollbackFor = ServiceException.class)
    public void persist(Zoo zoo) throws ServiceException {
        try {
            this.zooDao.persist(zoo);
            for (Animal animal : zoo.getAnimals())
                this.outboxService.persist(animal);
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    @Transactional(rollbackFor = ServiceException.class)
    public void persistNative(Zoo zoo) throws ServiceException {
        try {
            this.zooDao.persistNative(zoo);
            for (Animal animal : zoo.getAnimals())
                this.outboxService.persist(animal);
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    public void persistByBlocks(Zoo zoo) {
        this.zooDao.persistByBlocks(zoo);
    }

    @Override
    public Zoo findOne(int id) {
        return this.zooDao.findOne(id);
    }
}
