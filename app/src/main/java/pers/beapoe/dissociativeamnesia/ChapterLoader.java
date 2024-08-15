package pers.beapoe.dissociativeamnesia;

import android.content.res.AssetManager;
import android.os.Process;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class ChapterLoader {
    private ArrayList<Chapter> Chapters = new ArrayList<>();
    private final AssetManager assetManager;

    public ChapterLoader(AssetManager assetManager){
        this.assetManager = assetManager;
    }

    public ArrayList<Chapter> LoadChapters(){
        final String TAG = "ChapterLoader:LoadChapters";
        try {
            String[] files = assetManager.list("");
            if(files!=null) {
                for (String name : files) {
                    if(name.endsWith(".txt")) Chapters.add(new Chapter(name));
                }
            }else{
                Log.e(TAG,"No files found in assets");
                android.os.Process.killProcess(Process.myPid());
                System.exit(1);
            }
        } catch (IOException e) {
            Log.e(TAG,"Error getting list of files in assets",new IOException("获取assets文件夹下文件列表时出错"));
            android.os.Process.killProcess(Process.myPid());
            System.exit(1);
        }
        return Chapters;
    }
}
