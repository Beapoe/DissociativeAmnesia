package pers.beapoe.dissociativeamnesia;

import android.app.Application;
import android.os.Process;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

public class CustomApplication extends Application {
    private ArrayList<Chapter> Chapters = new ArrayList<>();
    private static final Gson gson = new Gson();
    private int CurrentReadPoint = 0;

    public String getNovelName() {
        return "解离性失忆";
    }

    public ArrayList<Chapter> getChapters() {
        return Chapters;
    }

    public void setChapters(ArrayList<Chapter> chapters) {
        Chapters = chapters;
    }

    public static String Serialize(Chapter toSerialize){
        return gson.toJson(toSerialize);
    }

    public static String SerializeList(ArrayList<Chapter> toSerialize){
        return gson.toJson(toSerialize);
    }

    public static Chapter Deserialize(String json){
        Type type = new TypeToken<Chapter>(){}.getType();
        return gson.fromJson(json,type);
    }

    public static ArrayList<Chapter> DeserializeList(String json){
        if(Objects.equals(json, "")){
            Log.e("CustomApplication:Deserialize(String json)","Data local save went wrong",new Exception("存储本地数据时出错"));
            android.os.Process.killProcess(Process.myPid());
            System.exit(1);
        }
        Type type = new TypeToken<ArrayList<Chapter>>(){}.getType();
        return gson.fromJson(json,type);
    }

    public int getCurrentReadPoint() {
        return CurrentReadPoint;
    }

    public void setCurrentReadPoint(int currentReadPoint) {
        CurrentReadPoint = currentReadPoint;
    }
}
