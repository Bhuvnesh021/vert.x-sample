package org.example.services;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClientDeleteResult;
import org.example.constants.Constants;
import org.example.listeners.RegisterUserListener;
import org.example.mapper.UserRequestJsonMapper;
import org.example.pojo.UserRequest;
import org.example.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class UserServiceImpl implements UserService{
    public UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private RegisterUserListener registerUserListener;
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public List<UserRequest> getUser() {

        return null;
    }

    @Override
    public UserRequest getUserWithId() {
        return null;
    }

    @Override
    public Future<String> addUser(UserRequest userRequest, Vertx vertx, HttpServerResponse response) {
        logger.error("add user called ");
        JsonObject json = UserRequestJsonMapper.toJson(userRequest);
        return userRepository.save(Constants.USER_COLLECTION, json, asyncResultHandler);

    }



    @Override
    public void updateUser(UserRequest userRequestObject, JsonObject query, Vertx vertx) {
        logger.error("updateUser called ");
        JsonObject json = UserRequestJsonMapper.toJson(userRequestObject);
        userRepository.update(Constants.USER_COLLECTION,json, query, asyncResultHandler);
    }

    @Override
    public void registerUserListener(RegisterUserListener listener) {
        this.registerUserListener = listener;
    }

    @Override
    public Future<List<JsonObject>> findUser(String collectionName, JsonObject query) {
       return userRepository.find(collectionName, query);
    }

    @Override
    public Future<Long> count(String collectionName, JsonObject query) {
        return userRepository.count(collectionName, query);
    }

    @Override
    public Future<MongoClientDeleteResult> delete(String collectionName, JsonObject query) {
        return userRepository.delete(collectionName, query);
    }

    private final Handler<AsyncResult<String>> asyncResultHandler = event -> {
        if (event.succeeded()) {
            registerUserListener.registered(event.result());
        } else {
            event.cause().printStackTrace();
        }
    };
}
