package org.example.handler;

import io.vertx.core.Vertx;
import io.vertx.ext.healthchecks.Status;
import io.vertx.ext.web.Router;

public class HealthCheckHandler {

    public static void setRouter(Vertx vertx,
                                 Router router) {
        final io.vertx.ext.healthchecks.HealthCheckHandler healthCheckHandler = io.vertx.ext.healthchecks.HealthCheckHandler.create(vertx);

        healthCheckHandler.register("database",
                promise ->
                        promise.complete(Status.OK()));

        healthCheckHandler.register("app",
                promise ->
                {
                    if(true) {
                        promise.complete(Status.OK());
                    }
                });


        router.get("/health").handler(healthCheckHandler);
    }
}

