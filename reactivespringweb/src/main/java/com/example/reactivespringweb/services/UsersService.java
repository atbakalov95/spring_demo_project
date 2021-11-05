package com.example.reactivespringweb.services;

import com.example.reactivespringweb.models.mongo.UserDocument;
import com.example.reactivespringweb.repos.UserCrudRepository;
import com.example.reactivespringweb.repos.UserDetailsTransactionalRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

@Service
public class UsersService {
    private final static Logger logger = Logger.getLogger(UsersService.class.getName());
    private final UserCrudRepository userCrudRepository;
    private final UserDetailsTransactionalRepository userTransactionalRepository;

    public UsersService(UserCrudRepository userCrudRepository, UserDetailsTransactionalRepository userTransactionalRepository) {
        this.userCrudRepository = userCrudRepository;
        this.userTransactionalRepository = userTransactionalRepository;
    }

    public Mono<UserDocument> insertUser(UserDocument userDocument){
        return userCrudRepository.save(userDocument);
    }

    public Flux<UserDocument> findAll() {
        return userCrudRepository.findAll();
    }

    public Flux<UserDocument> insertUsers(Iterable<UserDocument> users) {
        return userTransactionalRepository.insertUsers(users);
    }

    public Mono<UserDocument> getUserByName(String name) {
        return userCrudRepository.findOne(Example.of(
                UserDocument.builder()
                        .name(name)
                        .build()
        ));
    }
}
