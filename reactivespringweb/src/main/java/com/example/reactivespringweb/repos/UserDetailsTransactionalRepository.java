package com.example.reactivespringweb.repos;

import com.example.reactivespringweb.models.mongo.UserDocument;
import reactor.core.publisher.Flux;

public interface UserDetailsTransactionalRepository {
    Flux<UserDocument> insertUsers(Iterable<UserDocument> users);
}
