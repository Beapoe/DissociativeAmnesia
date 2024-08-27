package pers.beapoe.dissociativeamnesia;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Process;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

// 自定义应用程序类，继承自Application类
public class CustomApplication extends Application {
    // 定义一个Chapter类型的ArrayList，用于存储章节信息
    private ArrayList<Chapter> Chapters = new ArrayList<>();
    // 定义一个Gson对象，用于序列化和反序列化
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(SpannableStringBuilder.class,new SpannableStringBuilderAdapter())
            .registerTypeAdapter(ForegroundColorSpan.class,new ForegroundColorSpanAdapter())
            .create();
    // 定义一个int类型的变量，用于存储当前阅读的章节
    private int CurrentReadPoint = 0;
    // 定义一个int类型的变量，用于存储文本大小
    private int TextSize = 14;
    // 定义Span类别
    public enum SpanType{
        REPLACE
    }

    // 获取小说名称
    public String getNovelName() {
        return "解离性失忆";
    }

    // 获取章节列表
    public ArrayList<Chapter> getChapters() {
        return Chapters;
    }

    // 设置章节列表
    public void setChapters(ArrayList<Chapter> chapters) {
        Chapters = chapters;
    }

    // 序列化Chapter对象
    public static String Serialize(Chapter toSerialize){
        return gson.toJson(toSerialize);
    }

    // 序列化Chapter对象列表
    public static String SerializeList(ArrayList<Chapter> toSerialize){
        return gson.toJson(toSerialize);
    }

    // 反序列化Chapter对象
    public static Chapter Deserialize(String json){
        Type type = new TypeToken<Chapter>(){}.getType();
        return gson.fromJson(json,type);
    }

    // 反序列化Chapter对象列表
    public static ArrayList<Chapter> DeserializeList(String json){
        final String TAG = "CustomApplication:DeserializeList(...)";
        // 如果json为空，则抛出异常并退出程序
        if(Objects.equals(json, "")){
            Log.e(TAG,"Data local save went wrong",new Exception("存储本地数据时出错"));
            android.os.Process.killProcess(Process.myPid());
            System.exit(1);
        }
        Type type = new TypeToken<ArrayList<Chapter>>(){}.getType();
        return gson.fromJson(json,type);
    }

    // 获取当前阅读的章节
    public int getCurrentReadPoint() {
        return CurrentReadPoint;
    }

    // 设置当前阅读的章节
    public void setCurrentReadPoint(int currentReadPoint) {
        CurrentReadPoint = currentReadPoint;
    }

    // 获取文本大小
    public int getTextSize() {
        return TextSize;
    }

    // 设置文本大小
    public void setTextSize(int textSize) {
        TextSize = textSize;
    }

    public static Gson getGson() {
        return gson;
    }

    public void save(){
        SharedPreferences sp = getSharedPreferences("sp",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("Chapters",CustomApplication.SerializeList(getChapters()));
        editor.putInt("CurrentReadPoint",getCurrentReadPoint());
        editor.putInt("TextSize",getTextSize());
        editor.apply();
    }
}