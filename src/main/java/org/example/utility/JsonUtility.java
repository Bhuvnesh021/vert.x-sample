package org.example.utility;

import com.google.gson.Gson;
import io.vertx.core.json.JsonObject;

public class JsonUtility<T> {

    private static Gson gson = null;


    public static JsonObject getSinglePropertyJsonObject(String key, String value){
        return new JsonObject().put(key,value);
    }
    public static Gson getGson(){
        if(gson == null){
            gson = new Gson();
        }
        return gson;
    }

    public String toJson(T object){
        JsonUtility.getGson();
        return gson.toJson(object);
    }
}
