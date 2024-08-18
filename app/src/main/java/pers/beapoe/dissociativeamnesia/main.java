package pers.beapoe.dissociativeamnesia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class main extends AppCompatActivity {
    // 定义一个ActivityResultLauncher对象，用于启动其他Activity
    private ActivityResultLauncher<Intent> launcher;
    // 定义一个CustomApplication对象，用于获取全局变量
    CustomApplication app;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final String TAG = "Main:OnCreate(Bundle SavedInstanceState)";
        app = (CustomApplication) getApplication();
        // 获取TextView对象
        TextView Read = findViewById(R.id.Read);
        // 注册ActivityResultLauncher对象，用于启动其他Activity
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if(o.getResultCode()==RESULT_OK){
                    if(o.getData()!=null){
                        if(o.getData().getBooleanExtra("isSpecial",false)){
                            Gson gson = new Gson();
                            Type type = new TypeToken<SpannableStringBuilder>(){}.getType();
                            // 解析，设置
                            Read.setText(gson.fromJson(o.getData().getStringExtra("Content"),type));
                        }else{
                            Read.setText(o.getData().getStringExtra("Content"));
                        }
                    }
                }
            }
        });
        // 获取SharedPreferences对象
        SharedPreferences sp = getSharedPreferences("sp",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Log.i(TAG,String.valueOf(!sp.getBoolean("FirstRead", false)));
        // 判断是否是第一次读取
        if(!sp.getBoolean("FirstRead", false)){
            // 如果是第一次读取，则将FirstRead设置为true
            editor.putBoolean("FirstRead",true);
            // 获取Chapter对象列表
            ArrayList<Chapter> Chapters = ((CustomApplication)getApplication()).getChapters();
            // 加载Chapter对象列表
            ChapterLoader loader = new ChapterLoader(this.getAssets());
            Chapters = loader.LoadChapters(this);
            // 设置第一个Chapter为已读
            Chapters.get(0).setRead(true);
            app.setChapters(Chapters);
            // 将Chapter对象列表序列化并保存到SharedPreferences中
            editor.putString("Chapters",CustomApplication.SerializeList(Chapters));
            editor.putInt("CurrentReadPoint",0);
            // 提交SharedPreferences的修改
            if(!editor.commit()){
                Log.e("Main:OnCreate(Bundle SavedInstanceState)","Boolean data local save went wrong",new Exception("布尔数据本地储存时出错"));
                android.os.Process.killProcess(Process.myPid());
                System.exit(1);
            }
            // 加载第一个Chapter的内容
            String FirstChapterContent = Chapters.get(0).getResult();
            Read.setText(FirstChapterContent);
            //TODO:设计xml界面，加上将文本添加到界面的逻辑
        }else{
            Log.i(TAG,String.valueOf(!sp.getBoolean("FirstRead",false)));
            // 如果不是第一次读取，则从SharedPreferences中读取Chapter对象列表
            if(!sp.getBoolean("FirstRead",false)){
                Log.e("Main:OnCreate(Bundle SavedInstanceState)","Boolean data local read went wrong",new Exception("布尔数据本地读取时出错"));
                android.os.Process.killProcess(Process.myPid());
                System.exit(1);
            }else{
                ArrayList<Chapter> Chapters = CustomApplication.DeserializeList(sp.getString("Chapters",""));
                app.setChapters(Chapters);
                int ReadPoint = sp.getInt("CurrentReadPoint",0);
                app.setCurrentReadPoint(ReadPoint);
                Read.setTextSize(sp.getInt("TextSize",14));
                app.setTextSize(sp.getInt("TextSize",14));
                if(Chapters.get(ReadPoint).isSpecial()){
                    Read.setText(Chapters.get(ReadPoint).getContent());
                }else{
                    Read.setText(Chapters.get(ReadPoint).getResult());
                }
            }
        }
    }

    // 点击Contents按钮时，启动Contents Activity
    public void Contents_OnClick(View v){
        launcher.launch(new Intent(this,Contents.class));
    }

    // 点击Previous按钮时，读取上一个Chapter的内容
    public void Previous_OnClick(View v){
        int ReadPoint = app.getCurrentReadPoint();
        if(ReadPoint==0){
            Toast.makeText(getApplicationContext(),"最初之前，一片虚无",Toast.LENGTH_SHORT).show();
        }else{
            TextView Read = findViewById(R.id.Read);
            ArrayList<Chapter> Chapters = app.getChapters();
            int temp = ReadPoint-1;
            if(Chapters.get(temp).isSpecial()){
                Read.setText(Chapters.get(temp).getContent());
            }else{
                Read.setText(Chapters.get(temp).getResult());
            }
            app.setCurrentReadPoint(temp);
        }
    }

    // 点击Next按钮时，读取下一个Chapter的内容
    public void Next_OnClick(View v){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                NextOnClick(v);
            }
        },100);
    }
    public void NextOnClick(View v){
        int ReadPoint = app.getCurrentReadPoint();
        ArrayList<Chapter> Chapters = app.getChapters();
        if(ReadPoint==Chapters.size()-1){
            Toast.makeText(getApplicationContext(),"无尽之后，迷雾茫茫",Toast.LENGTH_SHORT).show();
        }else{
            int temp = ReadPoint+1;
            if(!Chapters.get(temp).isRead()) Chapters.get(temp).setRead(true);
            TextView Read = findViewById(R.id.Read);
            if(Chapters.get(temp).isSpecial()){
                SpannableStringBuilder content = Chapters.get(temp).getContent();
                Read.setText(content);
            }else{
                Read.setText(Chapters.get(temp).getResult());
            }
            app.setCurrentReadPoint(temp);
            app.setChapters(Chapters);
        }
    }

    // 点击BlowUp按钮时，放大文本
    public void BlowUp_OnClick(View v){
        final int MAX = 24;
        TextView Read = findViewById(R.id.Read);
        if(px2sp(Read.getTextSize(),this)==MAX){
            Toast.makeText(getApplicationContext(),"放大至极，细节隐于无形",Toast.LENGTH_SHORT).show();
        }else{
            Read.setTextSize(TypedValue.COMPLEX_UNIT_SP,px2sp(Read.getTextSize(),this)+2);
            app.setTextSize(px2sp(Read.getTextSize(),this)+2);
        }
    }

    // 点击Minificate按钮时，缩小文本
    public void Minificate_OnClick(View v){
        final int MIN = 8;
        TextView Read = findViewById(R.id.Read);
        if(px2sp(Read.getTextSize(),this)==MIN){
            Toast.makeText(getApplicationContext(),"细小入微，字隐虚空无痕",Toast.LENGTH_SHORT).show();
        }else{
            Read.setTextSize(TypedValue.COMPLEX_UNIT_SP,px2sp(Read.getTextSize(),this)-2);
            app.setTextSize(px2sp(Read.getTextSize(),this)-2);
        }
    }

    // 将像素值转换为sp值
    public static int px2sp(float pxValue, Context context) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sp = getSharedPreferences("sp",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("Chapters",CustomApplication.SerializeList(app.getChapters()));
        editor.putInt("CurrentReadPoint",app.getCurrentReadPoint());
        editor.putInt("TextSize",app.getTextSize());
        editor.apply();
    }
}
