//package com.example.reactivespringweb.configs;
//
//import com.mongodb.ConnectionString;
//import com.mongodb.MongoClientSettings;
//import com.mongodb.reactivestreams.client.MongoClient;
//import com.mongodb.reactivestreams.client.MongoClients;
//import org.springframework.context.annotation.Bean;
//import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
//import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
//
//import java.util.logging.Logger;
//
//@EnableReactiveMongoRepositories
//public class MongoConfig extends AbstractReactiveMongoConfiguration {
//    private final static Logger logger = Logger.getLogger(MongoConfig.class.getName());
//
//    @Bean
//    public MongoClient mongoClient() {
//        String mongoConnectionString = "mongodb://localhost:8088";
//        ConnectionString connectionString = new ConnectionString(mongoConnectionString);
//        logger.info("Connection string:"+connectionString);
//        logger.info("Credentials:"+connectionString.getCredential());
//
//        return MongoClients.create(connectionString);
//    }
//
//    @Override
//    protected String getDatabaseName() {
//        return "users";
//    }
//}
