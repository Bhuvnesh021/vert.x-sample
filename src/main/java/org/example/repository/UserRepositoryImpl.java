package org.example.repository;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.example.constants.Constants;
import org.example.database.DBConnection;
import org.example.database.MongoClientFactory;

public class UserRepositoryImpl implements UserRepository{


    public UserRepositoryImpl(){

    }
    @Override
    public void save(String collectionName, JsonObject jsonObject, Handler<AsyncResult<String>> handler) {
        MongoClient sharedInstance = MongoClientFactory.getInstance().createSharedInstance(Constants.DEMO_DB);
        System.out.println(sharedInstance);
        sharedInstance.save(collectionName, jsonObject).onComplete(handler);
    }

    @Override
    public void update(String collectionName, JsonObject jsonObject, JsonObject query, Handler<AsyncResult<String>> handler) {
        MongoClient sharedInstance = MongoClientFactory.getInstance().createSharedInstance(Constants.DEMO_DB);
        sharedInstance.updateCollection(collectionName, jsonObject, query);
    }
}
