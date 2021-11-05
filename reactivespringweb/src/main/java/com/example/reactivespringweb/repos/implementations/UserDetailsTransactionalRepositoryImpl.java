package com.example.reactivespringweb.repos.implementations;

import com.example.reactivespringweb.models.mongo.UserDocument;
import com.example.reactivespringweb.repos.UserDetailsTransactionalRepository;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class UserDetailsTransactionalRepositoryImpl implements UserDetailsTransactionalRepository {
    private final ReactiveMongoTemplate mongoTemplate;

    public UserDetailsTransactionalRepositoryImpl(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Flux<UserDocument> insertUsers(Iterable<UserDocument> users) {
        Mono<List<UserDocument>> inputFlux = Flux.fromIterable(users)
                .doOnNext(it -> Assert.isTrue(it.getAge() >= 18, "Age should be greater then 18"))
                .collect(Collectors.toList());

        return mongoTemplate.inTransaction().execute(action ->
                action.insertAll(inputFlux)
        );
    }
}
