package com.example.demo.defaultapp.services;

import com.example.demo.defaultapp.exceptions.ServiceException;
import com.example.demo.defaultapp.model.Zoo;

public interface ZooService {
    void persist(Zoo zoo) throws ServiceException;
    public void persistNative(Zoo zoo) throws ServiceException;
    void persistByBlocks(Zoo zoo);
    Zoo findOne(int id);
}
