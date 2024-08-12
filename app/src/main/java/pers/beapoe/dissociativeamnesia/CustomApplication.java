package pers.beapoe.dissociativeamnesia;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CustomApplication extends Application {
    private ArrayList<Chapter> Chapters = new ArrayList<>();

    public String getNovelName() {
        return "解离性失忆";
    }

    public ArrayList<Chapter> getChapters() {
        return Chapters;
    }

    public void setChapters(ArrayList<Chapter> chapters) {
        Chapters = chapters;
    }

    public static <T extends Serializable> String Serialize(T toSerialize){
        Gson gson = new Gson();
        return gson.toJson(toSerialize);
    }

    public static <T extends Serializable> String SerializeList(List<T> toSerialize){
        Gson gson = new Gson();
        return gson.toJson(toSerialize);
    }

    public static <T extends Serializable> T Deserialize(String json,Class<T> clazz){
        Gson gson =new Gson();
        Type type = new TypeToken<T>(){}.getType();
        return gson.fromJson(json,type);
    }

    public static <T extends Serializable> List<T> DeserializeList(String json,Class<T> clazz){
        Gson gson =new Gson();
        Type type = new TypeToken<List<T>>(){}.getType();
        return gson.fromJson(json,type);
    }

}
