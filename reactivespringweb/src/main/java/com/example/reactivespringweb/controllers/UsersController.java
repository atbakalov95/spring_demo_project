package com.example.reactivespringweb.controllers;

import com.example.reactivespringweb.helpers.StringHelpers;
import com.example.reactivespringweb.models.mongo.UserDocument;
import com.example.reactivespringweb.services.UsersService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

@RestController
@RequestMapping("/users")
public class UsersController {
    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/name={name}")
    public Mono<UserDocument> getUserByName(@PathVariable String name) {
        Logger.getLogger("test").info("Name:"+name);
        if (StringHelpers.isNullOrEmpty(name))
            return Mono.empty();
        return usersService.getUserByName(name);
    }
}
