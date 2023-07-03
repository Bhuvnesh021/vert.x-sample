package org.example.utility;

import com.google.gson.Gson;

public class JsonUtility<T> {

    private static Gson gson = null;



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
