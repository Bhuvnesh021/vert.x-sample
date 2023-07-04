package org.example.handler;

import com.google.gson.Gson;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClientDeleteResult;
import io.vertx.ext.web.RequestBody;
import io.vertx.ext.web.RoutingContext;
import org.eclipse.jgit.util.StringUtils;
import org.example.constants.Constants;
import org.example.listeners.CheckUserExistOrNot;
import org.example.listeners.RegisterUserListener;
import org.example.pojo.UserRequest;
import org.example.services.UserService;
import org.example.utility.JsonUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


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
        String userId = event.pathParam("userId");
        JsonObject jsonObject = new JsonObject();
        if(!StringUtils.isEmptyOrNull(userId)){
            jsonObject.put("userId" , Integer.parseInt(userId));
        }
        Future<List<JsonObject>> user =
                userService.findUser(Constants.USER_COLLECTION, jsonObject);
        event.response().putHeader("Content-Type", "application/json");
        user.onComplete(event1 -> {
           if(event1.succeeded()){
               List<JsonObject> result = event1.result();
               JsonObject response = new JsonObject();
               JsonArray jsonArray = new JsonArray();
               result.forEach(jsonArray::add);
               response.put("users" , jsonArray);
               event.response().end(response.encodePrettily());
           }else {
               jsonObject.put("error", "failed to fetch records due to "+ event1.cause().toString());
               event.response().end(jsonObject.encodePrettily());
           }
        });

    }

    public void addUser(RoutingContext context) {
        UserRequest userRequest = getUserRequestObject(context.body());
        logger.info(userRequest.toString());
        validateUserRequest(Constants.USER_COLLECTION, userRequest, checkUserExistOrNot, context);
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

        context.response().end("Test executed!!");


    }

    private final RegisterUserListener registerUserListener = userId -> {
        EventBus eventBus = vertx.eventBus();
        eventBus.request(Constants.REGISTER_IN_EXT_SYSTEM,userId);
    };

    private void validateUserRequest(String userCollection, UserRequest userRequest,final CheckUserExistOrNot validate, RoutingContext context) {
        JsonObject query = new JsonObject().put("userId", userRequest.getUserId());
        userService.count(userCollection, query).onComplete(event -> {
            if(event.succeeded() && event.result() > 0){
                 validate.exist(userRequest, context);
            } else {
                validate.notExist(userRequest, context);
            }
        });
    }

    private CheckUserExistOrNot checkUserExistOrNot = new CheckUserExistOrNot() {
        @Override
        public void exist(UserRequest userRequest, RoutingContext context) {
            context.response().putHeader("Content-Type", "application/json");
            context.response().end(JsonUtility.getSinglePropertyJsonObject("error","User already exist with userId: "+ userRequest.getUserId()).encodePrettily());
        }

        @Override
        public void notExist(UserRequest userRequest, RoutingContext context) {
            Future<String> stringFuture = userService.addUser(userRequest, context.vertx(), context.response());
            stringFuture.onComplete(event -> {
                if(event.succeeded()){
                    userRequest.set_id(stringFuture.result());
                    context.response().putHeader("Content-Type", "application/json");
                    context.response().end(JsonUtility.getGson().toJson(userRequest));

                    // start process - register user in ext system

                    EventBus eventBus = vertx.eventBus();
                    eventBus.request(Constants.REGISTER_IN_EXT_SYSTEM,stringFuture.result());
                }
            });
        }
    };

    public void delete(RoutingContext context) {
        String userId = context.pathParam("userId");
        if(userIdIsNotValid(userId)){
            context.response().end("Invalid User ID");
            return;
        }
        Integer userID = Integer.parseInt(userId);
        JsonObject query = new JsonObject().put("userId", userID);
        Future<MongoClientDeleteResult> delete = userService.delete(Constants.USER_COLLECTION, query);
        delete.onComplete(event -> {
            if(event.succeeded()){
                MongoClientDeleteResult result = event.result();
                JsonObject json = result.toJson();
                json.put("deleted", result.getRemovedCount());
                context.response().end(json.encodePrettily());
            }else {
                context.response().end("Faild to delete user due to >> " + event.cause().toString());
            }
        });

    }

    private boolean userIdIsNotValid(String userId) {
        if(StringUtils.isEmptyOrNull(userId))
            return true;
        try{
            Integer.parseInt(userId);
        }catch (Exception e){
            return true;
        }
        return false;
    }
}
