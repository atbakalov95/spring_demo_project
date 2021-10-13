package com.example.demo.defaultapp.services.implementation;

import com.example.demo.defaultapp.dao.AnimalDao;
import com.example.demo.defaultapp.exceptions.ServiceException;
import com.example.demo.defaultapp.model.Animal;
import com.example.demo.defaultapp.services.AnimalService;
import com.example.demo.defaultapp.services.OutboxService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class AnimalServiceImpl implements AnimalService {
    private static final Logger logger = Logger.getLogger(AnimalServiceImpl.class.getName());

    private final AnimalDao animalDao;
    private final OutboxService outboxService;

    public AnimalServiceImpl(AnimalDao animalDao, OutboxService outboxDao) {
        this.animalDao = animalDao;
        this.outboxService = outboxDao;
    }

    @Override
    @Transactional
    public void delete(int id) {
        this.animalDao.deleteById(id);
    }

    @Transactional(rollbackFor = ServiceException.class)
    public void persist(Animal animal) throws ServiceException {
        try {
            this.animalDao.persist(animal);
            this.outboxService.persist(animal);
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    public Animal findOne(int id) {
        return this.animalDao.findOne(id);
    }

    public List<Animal> findAll() {return this.animalDao.findAll();}

    @Override
    public List<Animal> findByOwner(String owner) {
        return this.animalDao.findByOwner(owner);
    }

    @Override
    public List<Animal> findByName(String name) {
        return this.animalDao.findByName(name);
    }
}
