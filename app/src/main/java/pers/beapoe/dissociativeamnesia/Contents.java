package pers.beapoe.dissociativeamnesia;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// 定义一个Contents类，继承自Activity
public class Contents extends Activity {
    // 重写onCreate方法
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置布局文件
        setContentView(R.layout.contents);

        // 获取RecyclerView控件
        RecyclerView rv = findViewById(R.id.menu);
        // 创建ContentsAdapter对象
        ContentsAdapter adapter = new ContentsAdapter(this);
        // 设置RecyclerView的布局管理器
        rv.setLayoutManager(new LinearLayoutManager(this));
        // 设置RecyclerView的适配器
        rv.setAdapter(adapter);
    }

    // 重写onStop方法
    @Override
    protected void onStop() {
        super.onStop();
        // 获取CustomApplication对象
        CustomApplication app = (CustomApplication)getApplication();
        app.save();
    }
}
