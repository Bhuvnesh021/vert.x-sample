package org.example.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;


public class ExtVerticle extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        Router router = Router.router(vertx);
        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8088,event -> {
                    if(event.succeeded()){
                        System.out.println("server started on 8088");
                    }else {
                        System.out.println("failed to start on port 8088");
                    }
                });
        router.post("/register").handler(BodyHandler.create()).handler(event -> {
            try{
                Thread.sleep(5000);
            }catch (Exception e){
                e.printStackTrace();;
            }
            event.response().end(new JsonObject().put("message", "User registered successfully with us").encodePrettily());
        });
    }
}
