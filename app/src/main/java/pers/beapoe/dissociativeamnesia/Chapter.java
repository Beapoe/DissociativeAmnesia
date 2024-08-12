package pers.beapoe.dissociativeamnesia;

import android.app.Activity;
import android.content.Context;
import android.os.Process;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Objects;

public class Chapter implements Serializable {
    private String path;
    private String name;

    public Chapter(String name){
        path = "assets/"+name;
        this.name = name;
    }

    public String getPath(){
        return path;
    }

    public String getName(){
        return name;
    }

    public boolean Check(Activity activity) throws FileNotFoundException {
        CustomApplication app = (CustomApplication) activity.getApplication();
        boolean result = false;
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String NovelName = br.readLine().replaceAll("\\s","");
            String ChapterName = br.readLine().replaceAll("\\s","");
            boolean FirstLine = NovelName.equals(app.getNovelName());
            boolean SecondLine = Objects.equals(RemoveSuffix(name), ChapterName);
            result =  FirstLine && SecondLine;
        } catch (IOException e) {
            Log.e("Chapter:Check(Activity activity)","Error reading file:"+name,new IOException("阅读文件："+name+"时出错"));
            Process.killProcess(Process.myPid());
            System.exit(1);
        }
        return result;
    }

    private static String RemoveSuffix(String raw){
        int index = raw.lastIndexOf(".txt");
        String result = "";
        if(index == -1){
            Log.e("Error","No suffix found",new RuntimeException("后缀未找到"));
            Process.killProcess(Process.myPid());
            System.exit(1);
        }else{
            result =  raw.substring(0,index)+raw.substring(index+ ".txt".length());
        }
        return result;
    }

    public String Load(Context context){
        String result = "";
        try (InputStream is = context.getAssets().open(name)){
            byte[] buffer = new byte[is.available()];
            //noinspection ResultOfMethodCallIgnored
            is.read(buffer);
            result = new String(buffer);
        }catch (IOException e) {
            Log.e("Chapter:Load(Context context)","Error reading file:"+name,new IOException("阅读文件："+name+"时出错"));
            Process.killProcess(Process.myPid());
            System.exit(1);
        }
        return result;
    }
}
