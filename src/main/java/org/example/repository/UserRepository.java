package org.example.repository;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

public interface UserRepository {
    void save(String collectionName, JsonObject jsonObject, Handler<AsyncResult<String>> handler);

    void update(String collectionName, JsonObject jsonObject, JsonObject query, Handler<AsyncResult<String>> handler);
}
