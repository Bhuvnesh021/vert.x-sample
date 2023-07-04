package org.example.verticles;

import io.vertx.core.*;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.SLF4JLogDelegateFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.example.constants.Constants;
import org.example.handler.HealthCheckHandler;
import org.example.handler.UserHandler;
import org.example.repository.UserRepository;
import org.example.repository.UserRepositoryImpl;
import org.example.services.UserService;
import org.example.services.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(MainVerticle.class);
    @Override
    public void start() {
        logger.error("MainVertical::start()");

        String logFactory = System.getProperty("org.vertx.logger-delegate-factory-class-name");
        if (logFactory == null) {
            System.setProperty("org.vertx.logger-delegate-factory-class-name",
                    SLF4JLogDelegateFactory.class.getName());
        }
        initializeDB();
        HttpServer httpServer = vertx.createHttpServer();
        Router router = Router.router(vertx);
        UserRepository userRepository = new UserRepositoryImpl();
        UserService userService = new UserServiceImpl(userRepository);
        UserHandler userHandler = new UserHandler(userService, vertx);
        HealthCheckHandler.setRouter(vertx, router);
        router.route(HttpMethod.PUT, "/updateUser").handler(BodyHandler.create()).handler(userHandler::updateUser);
        router.get("/getUser").handler(userHandler::getUser);
        router.get("/test").handler(userHandler::test);
        router.delete("/delete/:userId").handler(userHandler::delete);
        router.delete("/deleteAll").handler(userHandler::deleteAll);
        router.route(HttpMethod.GET,"/getUser/:userId").handler(userHandler::getUser);
        router.route(HttpMethod.POST,"/addUser").handler(BodyHandler.create()).handler(userHandler::addUser);

        httpServer.requestHandler(router).listen(8080, event -> {
            if(event.succeeded()){
                logger.info("server started!!");
            }else {
                logger.info("server failing to start!!");
            }
        });


    }

    private void initializeDB() {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(Constants.DEMO_DB);
        jsonArray.add(Constants.SAMPLE_RESTAURANTS);
        jsonArray.add(Constants.SAMPLE_TRAINING);
        jsonArray.add(Constants.SAMPLE_GEO_SPATIAL);
        jsonObject.put(Constants.DB_NAMES, jsonArray);
        vertx.eventBus().request(Constants.OBJECT_INIT, jsonObject.encode(), event -> {
            logger.info(event.result().body().toString());
        } );
    }

}
