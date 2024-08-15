package pers.beapoe.dissociativeamnesia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class main extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        SharedPreferences sp = getSharedPreferences("sp",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        TextView Read = findViewById(R.id.Read);
        CustomApplication app = (CustomApplication) getApplication();
        if(!sp.getBoolean("Read",false)){
            editor.putBoolean("FirstRead",true);
            ArrayList<Chapter> Chapters = ((CustomApplication)getApplication()).getChapters();
            Chapter FirstChapter = new Chapter("Chapter1.txt");
            FirstChapter.setRead(true);
            Chapters.add(FirstChapter);
            app.setChapters(Chapters);
            editor.putString("Chapters",CustomApplication.SerializeList(Chapters));
            editor.putInt("Current",0);
            editor.apply();
            try {
                if(FirstChapter.Check(this)){
                    Log.e("Main:OnCreate(Bundle savedInstanceState)","Novel name or FirstChapter name doesn't match");
                    Process.killProcess(Process.myPid());
                    System.exit(1);
                }else{
                    String FirstChapterContent = FirstChapter.Load(this);
                    Read.setText(FirstChapterContent);
                    //TODO:设计xml界面，加上将文本添加到界面的逻辑
                }
            } catch (FileNotFoundException e) {
                Log.e("Main:OnCreate(Bundle savedInstanceState)","Error reading file:"+ FirstChapter.getName(),new FileNotFoundException("阅读文件："+ FirstChapter.getName()+"时出错"));
                Process.killProcess(Process.myPid());
                System.exit(1);
            }
        }else{
            if(sp.getBoolean("FirstRead",false)){
                Log.e("Main:OnCreate(Bundle SavedInstanceState)","Boolean data local save went wrong",new Exception("布尔数据本地储存时出错"));
                android.os.Process.killProcess(Process.myPid());
                System.exit(1);
            }else{
                ArrayList<Chapter> Chapters = CustomApplication.DeserializeList(sp.getString("Chapters",""));
                int ReadPoint = sp.getInt("Current",0);
                Read.setText(Chapters.get(ReadPoint).Load(this));
            }
        }
    }

    public void Contents_OnClick(View v){
        startActivity(new Intent(this, Contents.class));
    }
}
