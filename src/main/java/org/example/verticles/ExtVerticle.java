package org.example.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ExtVerticle extends AbstractVerticle {
    private static Logger logger = LoggerFactory.getLogger(ExtVerticle.class);
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        Router router = Router.router(vertx);
        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8088,event -> {
                    if(event.succeeded()){
                        logger.info("server started on 8088");
                    }else {
                        logger.info("failed to start on port 8088");
                    }
                });
        router.post("/register").handler(BodyHandler.create()).handler(event -> {

            event.response().end(new JsonObject().put("message", "User registered successfully with us").encodePrettily());
        });
    }
}
