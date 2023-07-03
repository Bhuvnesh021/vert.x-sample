package org.example.services;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.example.listeners.RegisterUserListener;
import org.example.pojo.UserRequest;

import java.net.http.HttpResponse;
import java.util.List;

public interface UserService {
    List<UserRequest> getUser();
    UserRequest getUserWithId();

    void addUser(UserRequest userRequest, Vertx vertx);

    void updateUser(UserRequest userRequestObject, JsonObject query, Vertx vertx);

    void registerUserListener(RegisterUserListener listener);
}
