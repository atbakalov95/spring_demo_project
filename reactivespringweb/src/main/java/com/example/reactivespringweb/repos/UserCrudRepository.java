package com.example.reactivespringweb.repos;

import com.example.reactivespringweb.models.mongo.UserDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCrudRepository extends ReactiveMongoRepository<UserDocument, String> { }
