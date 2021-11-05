package com.example.reactivespringweb.controllers;

import com.example.reactivespringweb.helpers.StringHelpers;
import com.example.reactivespringweb.models.mongo.UserDocument;
import com.example.reactivespringweb.services.UsersService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @GetMapping("/test")
    public Flux<UserDocument> testImport(@RequestParam("name") String name, @RequestParam("age") double age) {
        Stream<UserDocument> stream = Stream.of(4, 3, 2, 1)
                .map(i -> UserDocument.builder()
                        .name(name + i)
                        .age(age + i)
                        .build()
                );
        return usersService.insertUsers(stream.collect(Collectors.toList()));
    }

    @GetMapping("/all")
    public Flux<UserDocument> getAll() {
        return usersService.findAll();
    }
}
