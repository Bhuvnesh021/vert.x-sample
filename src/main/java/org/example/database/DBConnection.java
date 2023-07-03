package org.example.database;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DBConnection {
    private static final Map<String, MongoClient> concurrentHashMap = new ConcurrentHashMap<>();
    public static MongoClient getConnection(Vertx vertx, String dbName){
        if(concurrentHashMap.get(dbName) == null){
            concurrentHashMap.put(dbName,MongoClient.createShared(vertx, getConfigurations(dbName)));
        }
        return concurrentHashMap.get(dbName);
    }

    private static JsonObject getConfigurations(String dbName){
        JsonObject config = new JsonObject();
        config.put("db_name",dbName);
        config.put("username","vertx");
        config.put("password","vertx");
        config.put("connectTimeoutMS",300000);
        config.put("socketTimeoutMS",100000);
        config.put("maxIdleTimeMS",300000);
        config.put("maxLifeTimeMS",3600000);
        config.put("trustAll",true);
        config.put("connection_string","mongodb+srv://vertx:vertx@cluster0.ttsjljd.mongodb.net/?retryWrites=true&w=majority");
        return config;
    }

    public static MongoClient getConnection(String dbName) {
        return concurrentHashMap.get(dbName);
    }
}
