package com.example.demo.resourceserver;

import com.example.demo.resourceserver.dao.UserDao;
import com.example.demo.resourceserver.model.Animal;
import com.example.demo.resourceserver.model.Feline;
import com.example.demo.resourceserver.model.User;
import com.example.demo.resourceserver.services.AnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BaseController {

    @Autowired
    private AnimalService animalService;

    @RequestMapping(value = "/hello")
    public String sayHello() {
        return "Hello authorized user!";
    }
}
