package com.example.demo.defaultapp.services;

import com.example.demo.defaultapp.exceptions.ServiceException;
import com.example.demo.defaultapp.model.Animal;

import java.util.List;

public interface AnimalService {
    void delete(int id);
    void persist(Animal animal) throws ServiceException;
    Animal findOne(int id);
    List<Animal> findAll();
    List<Animal> findByOwner(String owner);
    List<Animal> findByName(String name);
}
