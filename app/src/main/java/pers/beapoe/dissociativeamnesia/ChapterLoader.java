package pers.beapoe.dissociativeamnesia;

import android.content.res.AssetManager;
import android.os.Process;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

// 定义一个ChapterLoader类，用于加载章节
public class ChapterLoader {
    // 定义一个Chapter类型的ArrayList，用于存储章节
    private ArrayList<Chapter> Chapters = new ArrayList<>();
    // 定义一个AssetManager类型的变量，用于管理assets文件夹
    private final AssetManager assetManager;

    // 构造函数，传入一个AssetManager类型的参数
    public ChapterLoader(AssetManager assetManager){
        this.assetManager = assetManager;
    }

    // 定义一个LoadChapters方法，用于加载章节
    public ArrayList<Chapter> LoadChapters(){
        // 定义一个TAG常量，用于日志输出
        final String TAG = "ChapterLoader:LoadChapters";
        try {
            // 获取assets文件夹下的所有文件名
            String[] files = assetManager.list("");
            // 如果文件名不为空
            if(files!=null) {
                // 遍历文件名
                for (String name : files) {
                    // 如果文件名以.txt结尾
                    if(name.endsWith(".txt")) {
                        // 将文件名添加到Chapters中
                        Chapters.add(new Chapter(name));
                    }
                }
            }else{
                // 如果文件名为空，输出错误日志，并结束进程
                Log.e(TAG,"No files found in assets");
                android.os.Process.killProcess(Process.myPid());
                System.exit(1);
            }
        } catch (IOException e) {
            // 如果获取文件列表时出错，输出错误日志，并结束进程
            Log.e(TAG,"Error getting list of files in assets",new IOException("获取assets文件夹下文件列表时出错"));
            android.os.Process.killProcess(Process.myPid());
            System.exit(1);
        }
        // 返回Chapters
        return Chapters;
    }
}
