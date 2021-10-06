package com.example.demo.resourceserver.services;

import com.example.demo.resourceserver.exceptions.ServiceException;
import com.example.demo.resourceserver.model.Zoo;

public interface ZooService {
    void persist(Zoo zoo) throws ServiceException;
    public void persistNative(Zoo zoo) throws ServiceException;
    void persistByBlocks(Zoo zoo);
    Zoo findOne(int id);
}
