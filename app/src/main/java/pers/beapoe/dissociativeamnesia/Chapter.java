package pers.beapoe.dissociativeamnesia;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Process;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

public class Chapter implements Serializable {
    // 定义章节名称
    private String name;
    // 定义是否已读
    private boolean isRead = false;
    // 定义章节名称
    private String ChapterName;

    // 构造函数，传入章节名称
    public Chapter(String name){if(!Objects.equals(name, "images") && !Objects.equals(name, "webkit")) this.name = name;}

    // 设置章节名称
    public void setName(String name){this.name = name;}
    // 获取章节名称
    public String getName(){
        return name;
    }

    // 检查章节是否匹配
    public boolean Check(Activity activity) throws FileNotFoundException {
        CustomApplication app = (CustomApplication) activity.getApplication();
        boolean result = false;
        try {
            AssetManager assetManager = activity.getAssets();
            InputStream is = assetManager.open(name);
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String NovelName = br.readLine().replaceAll("\\s","").substring(1);
            result = NovelName.equals(app.getNovelName());
        } catch (IOException e) {
            Log.e("Chapter:Check(Activity activity)","Error reading file:"+name,new IOException("阅读文件："+name+"时出错"));
            Process.killProcess(Process.myPid());
            System.exit(1);
        }
        return result;
    }

    // 加载章节内容
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

    // 获取是否已读
    public boolean isRead() {
        return isRead;
    }

    // 设置是否已读
    public void setRead(boolean read) {
        isRead = read;
    }

    // 获取章节名称
    public String getChapterName() {
        return ChapterName;
    }
}
