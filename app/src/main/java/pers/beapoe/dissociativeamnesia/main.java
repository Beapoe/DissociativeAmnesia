package pers.beapoe.dissociativeamnesia;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;

public class main extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        SharedPreferences sp = getSharedPreferences("sp",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if(!getSharedPreferences("sp",MODE_PRIVATE).getBoolean("Read",false)){
            editor.putBoolean("Read",true);
            Chapter FirstChapter = new Chapter("「序」.txt");
            try {
                if(!FirstChapter.Check(this)){
                    Log.e("Main:OnCreate(Bundle savedInstanceState)","Novel name or FirstChapter name doesn't match");
                    Process.killProcess(Process.myPid());
                    System.exit(1);
                }else{
                    String FirstChapterContent = FirstChapter.Load(this);
                    //TODO:设计xml界面，加上将文本添加到界面的逻辑
                }
            } catch (FileNotFoundException e) {
                Log.e("Main:OnCreate(Bundle savedInstanceState)","Error reading file:"+ FirstChapter.getName(),new FileNotFoundException("阅读文件："+ FirstChapter.getName()+"时出错"));
                Process.killProcess(Process.myPid());
                System.exit(1);
            }
        }
    }
}
