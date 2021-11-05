package com.example.reactivespringweb.services;

import com.example.reactivespringweb.models.mongo.UserDocument;
import com.example.reactivespringweb.repos.UserCrudRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

@Service
public class UsersService {
    private final static Logger logger = Logger.getLogger(UsersService.class.getName());
    private final UserCrudRepository userCrudRepository;

    public UsersService(UserCrudRepository userCrudRepository) {
        this.userCrudRepository = userCrudRepository;
    }

    public Mono<UserDocument> insertUser(UserDocument userDocument){
        Mono<UserDocument> documentMono = this.userCrudRepository.save(userDocument);
        documentMono.subscribe();
        return documentMono;
    }

    public Mono<UserDocument> getUserByName(String name) {
        return userCrudRepository.findOne(Example.of(
                UserDocument.builder()
                        .name(name)
                        .build()
        ));
    }
}
