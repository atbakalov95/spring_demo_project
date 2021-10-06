package com.example.demo.resourceserver.services;

import com.example.demo.resourceserver.exceptions.ServiceException;
import com.example.demo.resourceserver.model.Animal;

import java.util.List;

public interface AnimalService {
    void persist(Animal animal) throws ServiceException;
    Animal findOne(int id);
    List<Animal> findAll();
    List<Animal> findByOwner(String owner);
    List<Animal> findByName(String name);
}
