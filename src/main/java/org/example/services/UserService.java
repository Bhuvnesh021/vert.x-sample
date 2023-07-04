package org.example.services;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClientDeleteResult;
import org.example.listeners.RegisterUserListener;
import org.example.pojo.UserRequest;

import java.util.List;

public interface UserService {
    List<UserRequest> getUser();
    UserRequest getUserWithId();

    Future<String> addUser(UserRequest userRequest, Vertx vertx, HttpServerResponse response);

    void updateUser(UserRequest userRequestObject, JsonObject query, Vertx vertx);

    void registerUserListener(RegisterUserListener listener);

    Future<List<JsonObject>> findUser(String collectionName, JsonObject query);

    Future<Long> count(String collectionName, JsonObject query);
    Future<MongoClientDeleteResult> delete(String collectionName, JsonObject query);

}
