package org.example.database;

import io.vertx.ext.mongo.MongoClient;

public class MongoClientFactory {
    private static MongoClientFactory mongoClientFactory = null;
    private MongoClientFactory(){}

    public static MongoClientFactory getInstance(){
        if(mongoClientFactory == null) {
            mongoClientFactory = new MongoClientFactory();
        }
        return mongoClientFactory;
    }
    public MongoClient createSharedInstance(String dbName){
        return DBConnection.getConnection(dbName);
    }
}
