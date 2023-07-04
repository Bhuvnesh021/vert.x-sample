package org.example.repository;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.MongoClientDeleteResult;
import org.example.constants.Constants;
import org.example.database.MongoClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserRepositoryImpl implements UserRepository{

    private static Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);
    public UserRepositoryImpl(){

    }
    @Override
    public Future<String> save(String collectionName, JsonObject jsonObject, Handler<AsyncResult<String>> handler) {
        MongoClient sharedInstance = MongoClientFactory.getInstance().createSharedInstance(Constants.DEMO_DB);
        return sharedInstance.save(collectionName, jsonObject);
    }

    @Override
    public void update(String collectionName, JsonObject jsonObject, JsonObject query, Handler<AsyncResult<String>> handler) {
        MongoClient sharedInstance = MongoClientFactory.getInstance().createSharedInstance(Constants.DEMO_DB);
        sharedInstance.updateCollection(collectionName, jsonObject, query);
    }

    @Override
    public Future<List<JsonObject>> find(String collectionName, JsonObject query) {
        MongoClient sharedInstance = MongoClientFactory.getInstance().createSharedInstance(Constants.DEMO_DB);
        return sharedInstance.find(collectionName, query);
    }

    @Override
    public Future<Long> count(String collectionName, JsonObject query) {
        MongoClient sharedInstance = MongoClientFactory.getInstance().createSharedInstance(Constants.DEMO_DB);
        return sharedInstance.count(collectionName, query);
    }

    @Override
    public Future<MongoClientDeleteResult> delete(String collectionName, JsonObject query) {
        MongoClient sharedInstance = MongoClientFactory.getInstance().createSharedInstance(Constants.DEMO_DB);
        return sharedInstance.removeDocuments(collectionName, query);
    }
}
