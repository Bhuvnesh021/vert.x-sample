package org.example.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import org.example.constants.Constants;
import org.example.database.DBConnection;
import org.example.handler.UserHandler;
import org.example.repository.UserRepository;
import org.example.repository.UserRepositoryImpl;
import org.example.services.UserService;
import org.example.services.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(WorkerVerticle.class);
    @Override
    public void start() throws Exception {
        logger.error("WorkerVerticle called!!");
        EventBus eventBus = vertx.eventBus();

        eventBus.consumer("worker.thread", event -> {
            logger.info("it was received message : "+ event.body());
           event.reply("message reached!");
        });
        eventBus.consumer(Constants.MESSAGE_PATH, event -> {
            logger.error("<<< Message Received {}" , event.body());
            event.reply("sending back reply");
        });
        eventBus.consumer(Constants.OBJECT_INIT, event -> {
            JsonObject jsonObject = new JsonObject(event.body().toString());
            for (Object o : jsonObject.getJsonArray(Constants.DB_NAMES)) {
                DBConnection.getConnection(vertx, (String) o);
            }
            event.reply("Database connections initialized. [ "+ jsonObject+ " ]");
        });

        eventBus.consumer(Constants.REGISTER_IN_EXT_SYSTEM, event -> {

            String message = event.body().toString();
            WebClient webClient = WebClient.create(vertx);
            Future<HttpResponse<JsonObject>> accept = webClient.post(8088, "localhost", "/register")
                    .putHeader("Accept", "application/json")
                    .as(BodyCodec.jsonObject()).send();
            accept.onComplete(event1 -> {
                if(event1.succeeded()){
                    JsonObject body = event1.result().body();
                    logger.info(body.toString());

                }else {
                    event1.cause().printStackTrace();
                }
            });
            event.reply("call to ext system done");

        });
        logger.error("receiver ready !!");
    }
}
