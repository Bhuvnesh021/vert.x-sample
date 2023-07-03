package org.example.mapper;

import io.vertx.core.json.JsonObject;
import org.example.pojo.UserRequest;
import org.example.utility.JsonUtility;

public class UserRequestJsonMapper {
    public static JsonObject toJson(UserRequest userRequest){
        JsonUtility<UserRequest> jsonUtility = new JsonUtility<>();
        return new JsonObject(jsonUtility.toJson(userRequest));
    }
}
