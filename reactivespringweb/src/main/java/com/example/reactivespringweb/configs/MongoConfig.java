//package com.example.reactivespringweb.configs;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
//import org.springframework.data.mongodb.ReactiveMongoTransactionManager;
//import org.springframework.transaction.ReactiveTransactionManager;
//import org.springframework.transaction.reactive.TransactionalOperator;
//
//import java.util.logging.Logger;
//
//@Configuration
//public class MongoConfig {
//    private final static Logger logger = Logger.getLogger(MongoConfig.class.getName());
//
//    @Bean
//    public ReactiveMongoTransactionManager transactionManager(ReactiveMongoDatabaseFactory rdbf) {
//        return new ReactiveMongoTransactionManager(rdbf);
//    }
//
//    @Bean
//    public TransactionalOperator transactionalOperator(ReactiveTransactionManager rtm) {
//        return TransactionalOperator.create(rtm);
//    }
//}
