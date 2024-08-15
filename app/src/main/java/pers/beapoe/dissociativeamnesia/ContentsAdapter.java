package pers.beapoe.dissociativeamnesia;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

// 定义一个ContentsAdapter类，继承自RecyclerView.Adapter<ContentsAdapter.ContentsViewHolder>
public class ContentsAdapter extends RecyclerView.Adapter<ContentsAdapter.ContentsViewHolder>{
    // 定义一个ArrayList类型的Chapters变量，用于存储章节信息
    private ArrayList<Chapter> Chapters;
    // 定义一个Activity类型的activity变量，用于存储当前Activity
    private final Activity activity;

    // 构造函数，传入一个Activity类型的参数
    public ContentsAdapter(Activity activity){
        this.activity = activity;
        // 从CustomApplication中获取章节信息
        Chapters = ((CustomApplication)activity.getApplication()).getChapters();
    }

    // 重写onCreateViewHolder方法，用于创建ViewHolder
    @NonNull
    @Override
    public ContentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 从parent中获取LayoutInflater，并使用R.layout.content布局文件创建View
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content,parent,false);
        // 返回一个ContentsViewHolder对象
        return new ContentsViewHolder(view);
    }

    // 重写onBindViewHolder方法，用于绑定ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ContentsViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // 如果当前章节已读
        if(Chapters.get(position).isRead()){
            // 设置TextView的文本为章节名称
            holder.content.setText(Chapters.get(position).getChapterName());
            // 设置TextView的点击事件
            holder.content.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    // 创建一个Intent对象
                    Intent intent = new Intent();
                    // 将当前章节的内容传递给Intent
                    intent.putExtra("Content",Chapters.get(position).Load(activity));
                    // 设置Activity的结果码为RESULT_OK
                    activity.setResult(Activity.RESULT_OK,intent);
                    // 结束当前Activity
                    activity.finish();
                }
            });
        }else{
            // 如果当前章节未读，设置TextView的文本为"恭喜发现彩蛋"
            String surprise = "恭喜发现彩蛋";
            // 将"恭喜发现彩蛋"转换为ISO_8859_1编码的字节数组
            byte[] temp = surprise.getBytes(StandardCharsets.ISO_8859_1);
            // 将字节数组转换为UTF_8编码的字符串，并设置TextView的文本
            holder.content.setText(new String(temp, StandardCharsets.UTF_8));
        }

    }

    // 重写getItemCount方法，返回Chapters的大小
    @Override
    public int getItemCount() {
        return Chapters.size();
    }

    // 定义一个ContentsViewHolder类，继承自RecyclerView.ViewHolder
    public static class ContentsViewHolder extends RecyclerView.ViewHolder{
        // 定义一个TextView类型的content变量，用于存储TextView
        public TextView content;

        // 构造函数，传入一个View类型的参数
        public ContentsViewHolder(@NonNull View itemView) {
            super(itemView);
            // 从itemView中获取TextView，并赋值给content变量
            content = itemView.findViewById(R.id.content);
        }
    }

    // 定义一个getChapters方法，返回Chapters
    public ArrayList<Chapter> getChapters() {
        return Chapters;
    }

    // 定义一个setChapters方法，设置Chapters
    public void setChapters(ArrayList<Chapter> chapters) {
        Chapters = chapters;
    }
}