package org.example.handler;

import com.google.gson.Gson;
import io.netty.util.Constant;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RequestBody;
import io.vertx.ext.web.RoutingContext;
import org.example.constants.Constants;
import org.example.database.DBConnection;
import org.example.listeners.RegisterUserListener;
import org.example.pojo.UserRequest;
import org.example.services.UserService;
import org.example.services.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UserHandler {

    private UserService userService = null;

    private Vertx vertx;
    public UserHandler(UserService userService, Vertx vertx){
        this.userService= userService;
        this.userService.registerUserListener(registerUserListener);
        this.vertx = vertx;
    }

    public UserHandler(Vertx vertx){}
    private static Logger logger = LoggerFactory.getLogger(UserHandler.class);
    
    public void getUser(RoutingContext event){
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("userName" , "Bhuvnesh Jain");
        jsonObject.put("City", "Bangalore");
        logger.error("user error : {}", jsonObject.encode());
        event.response().end(jsonObject.encodePrettily());

    }

    public void addUser(RoutingContext context) {
        UserRequest userRequest = getUserRequestObject(context.body());
        System.out.println(userRequest);
        userService.addUser(userRequest, context.vertx());
        context.response().end("POST request handled!!");
    }


    public void updateUser(RoutingContext context) {
        String userName = context.pathParam("userName");
        String userId = context.pathParam("userId");
        logger.error("userId : {}",userId);
        logger.error("userName : {}",userName);
        JsonObject query = new JsonObject().put("userName", userName);
        UserRequest userRequestObject = getUserRequestObject(context.body());
        userService.updateUser(userRequestObject, query, context.vertx());
    }

    private static UserRequest getUserRequestObject(RequestBody requestBody){
        JsonObject jsonObject = requestBody.asJsonObject();
        Gson gson = new Gson();
        return gson.fromJson(jsonObject.encodePrettily(), UserRequest.class);
    }

    public void test(RoutingContext context) {
        EventBus eventBus = context.vertx().eventBus();
        eventBus.request(Constants.WORKER_THREAD,"testing...", event -> {
            if(event.succeeded()){
                System.out.println("message sent");
                System.out.println("getting reply : "+ event.result().body());
            }else {
                System.out.println("message sending failed..");
            }
        });

        context.response().end("Test executed!!");


    }

    private final RegisterUserListener registerUserListener = userId -> {
        EventBus eventBus = vertx.eventBus();
        eventBus.request(Constants.REGISTER_IN_EXT_SYSTEM,userId);
    };
}
