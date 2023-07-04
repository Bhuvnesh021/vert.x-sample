package org.example.listeners;

import io.vertx.ext.web.RoutingContext;
import org.example.pojo.UserRequest;

public interface CheckUserExistOrNot {
    void exist(UserRequest userRequest, RoutingContext context);

    void notExist(UserRequest userRequest, RoutingContext context);
}
