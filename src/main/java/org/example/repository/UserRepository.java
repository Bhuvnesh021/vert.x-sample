package org.example.repository;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClientDeleteResult;

import java.util.List;

public interface UserRepository {
    Future<String> save(String collectionName, JsonObject jsonObject, Handler<AsyncResult<String>> handler);

    void update(String collectionName, JsonObject jsonObject, JsonObject query, Handler<AsyncResult<String>> handler);

    Future<List<JsonObject>> find(String collectionName, JsonObject query);
    Future<Long> count(String collectionName, JsonObject query);
    Future<MongoClientDeleteResult> delete(String collectionName, JsonObject query);

}
