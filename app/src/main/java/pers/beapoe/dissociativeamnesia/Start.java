package pers.beapoe.dissociativeamnesia;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;

public class Start extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = getSharedPreferences("sp",MODE_PRIVATE);
        if(!sp.getBoolean("DoNotShow",false)){
            setContentView(R.layout.start);
            CheckBox cb = findViewById(R.id.DoNotShow);
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putBoolean("DoNotShow", true);
                        if (!editor.commit()) {
                            Log.e("Start:OnCreate(SavedInstanceState)", "Boolean data local save went wrong", new Exception("布尔数据本地储存时出错"));
                            android.os.Process.killProcess(Process.myPid());
                            System.exit(1);
                        }
                    }
                }
            });
        }else{
            Intent intent = new Intent(this, main.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public void Verify_OnClick(View v){
        Intent intent = new Intent(this, main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
