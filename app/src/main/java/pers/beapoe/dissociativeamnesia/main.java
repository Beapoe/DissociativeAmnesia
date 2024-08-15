package pers.beapoe.dissociativeamnesia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class main extends AppCompatActivity {
    private ActivityResultLauncher<Intent> launcher;
    CustomApplication app;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final String TAG = "Main:OnCreate(Bundle SavedInstanceState)";
        app = (CustomApplication) getApplication();
        TextView Read = findViewById(R.id.Read);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if(o.getResultCode()==RESULT_OK){
                    if(o.getData()!=null){
                        Read.setText(o.getData().getStringExtra("Content"));
                    }
                }
            }
        });
        SharedPreferences sp = getSharedPreferences("sp",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Log.i(TAG,String.valueOf(!sp.getBoolean("FirstRead", false)));
        if(!sp.getBoolean("FirstRead", false)){
            editor.putBoolean("FirstRead",true);
            ArrayList<Chapter> Chapters = ((CustomApplication)getApplication()).getChapters();
            ChapterLoader loader = new ChapterLoader(this.getAssets());
            Chapters = loader.LoadChapters();
            Chapters.get(0).setRead(true);
            app.setChapters(Chapters);
            editor.putString("Chapters",CustomApplication.SerializeList(Chapters));
            editor.putInt("Current",0);
            if(!editor.commit()){
                Log.e(TAG,"Boolean data local save went wrong",new Exception("布尔数据本地储存时出错"));
                android.os.Process.killProcess(Process.myPid());
                System.exit(1);
            }
            try {
                if(Chapters.get(0).Check(this)){
                    Log.e("Main:OnCreate(Bundle savedInstanceState)","Novel name or FirstChapter name doesn't match");
                    Process.killProcess(Process.myPid());
                    System.exit(1);
                }else{
                    String FirstChapterContent = Chapters.get(0).Load(this);
                    Read.setText(FirstChapterContent);
                    //TODO:设计xml界面，加上将文本添加到界面的逻辑
                }
            } catch (FileNotFoundException e) {
                Log.e("Main:OnCreate(Bundle savedInstanceState)","Error reading file:"+ Chapters.get(0).getName(),new FileNotFoundException("阅读文件："+ Chapters.get(0).getName()+"时出错"));
                Process.killProcess(Process.myPid());
                System.exit(1);
            }
        }else{
            if(!sp.getBoolean("FirstRead",false)){
                Log.e("Main:OnCreate(Bundle SavedInstanceState)","Boolean data local save went wrong",new Exception("布尔数据本地储存时出错"));
                android.os.Process.killProcess(Process.myPid());
                System.exit(1);
            }else{
                ArrayList<Chapter> Chapters = CustomApplication.DeserializeList(sp.getString("Chapters",""));
                app.setChapters(Chapters);
                int ReadPoint = sp.getInt("Current",0);
                app.setCurrentReadPoint(ReadPoint);
                Read.setText(Chapters.get(ReadPoint).Load(this));
            }
        }
    }

    public void Contents_OnClick(View v){
        launcher.launch(new Intent(this,Contents.class));
    }

    public void Previous_OnClick(View v){
        int ReadPoint = app.getCurrentReadPoint();
        if(ReadPoint==0){
            Toast.makeText(getApplicationContext(),"最初之前，一片虚无",Toast.LENGTH_SHORT).show();
        }else{
            TextView Read = findViewById(R.id.Read);
            ArrayList<Chapter> Chapters = app.getChapters();
            Read.setText(Chapters.get(ReadPoint--).Load(this));
            app.setCurrentReadPoint(ReadPoint--);
        }
    }

    public void Next_OnClick(View v){
        int ReadPoint = app.getCurrentReadPoint();
        ArrayList<Chapter> Chapters = app.getChapters();
        if(ReadPoint==Chapters.size()){
            Toast.makeText(getApplicationContext(),"无尽之后，迷雾茫茫",Toast.LENGTH_SHORT).show();
        }else{
            if(!Chapters.get(ReadPoint++).isRead()) Chapters.get(ReadPoint++).setRead(true);
            TextView Read = findViewById(R.id.Read);
            Read.setText(Chapters.get(ReadPoint++).Load(this));
            app.setCurrentReadPoint(ReadPoint++);
            app.setChapters(Chapters);
        }
    }

}
