package pers.beapoe.dissociativeamnesia;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Process;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

public class Chapter implements Serializable {
    private String name;
    private boolean isRead = false;
    private String ChapterName;

    public Chapter(String name){
        this.name = name;
    }

    public void setName(String name){this.name = name;}
    public String getName(){
        return name;
    }

    public boolean Check(Activity activity) throws FileNotFoundException {
        CustomApplication app = (CustomApplication) activity.getApplication();
        boolean result = false;
        try {
            AssetManager assetManager = activity.getAssets();
            InputStream is = assetManager.open(name);
            BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            String NovelName = br.readLine().replaceAll("\\s","");
            result = NovelName.equals(app.getNovelName());
        } catch (IOException e) {
            Log.e("Chapter:Check(Activity activity)","Error reading file:"+name,new IOException("阅读文件："+name+"时出错"));
            Process.killProcess(Process.myPid());
            System.exit(1);
        }
        return result;
    }

    public String Load(Context context){
        String result = "";
        try{
            ArrayList<String> lines = new ArrayList<>();
            AssetManager assetManager = context.getAssets();
            InputStream is = assetManager.open(name);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            String line;
            while((line = reader.readLine())!=null){
                lines.add(line);
            }
            lines.remove(0);
            ChapterName = lines.get(0);
            reader.close();
             result = String.join("\n",lines);
        }catch (IOException e) {
            Log.e("Chapter:Load(Context context)","Error reading file:"+name,new IOException("阅读文件："+name+"时出错"));
            Process.killProcess(Process.myPid());
            System.exit(1);
        }
        return result;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getChapterName() {
        return ChapterName;
    }

    public void setChapterName(String chapterName) {
        ChapterName = chapterName;
    }
}
