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

public class ContentsAdapter extends RecyclerView.Adapter<ContentsAdapter.ContentsViewHolder>{
    private ArrayList<Chapter> Chapters;
    private final Activity activity;

    public ContentsAdapter(Activity activity){
        this.activity = activity;
        Chapters = ((CustomApplication)activity.getApplication()).getChapters();
    }

    @NonNull
    @Override
    public ContentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content,parent,false);
        return new ContentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentsViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if(Chapters.get(position).isRead()){
            holder.content.setText(Chapters.get(position).getChapterName());
            holder.content.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("Content",Chapters.get(position).Load(activity));
                    activity.setResult(Activity.RESULT_OK,intent);
                    activity.finish();
                }
            });
        }else{
            String surprise = "恭喜发现彩蛋";
            byte[] temp = surprise.getBytes(StandardCharsets.ISO_8859_1);
            holder.content.setText(new String(temp, StandardCharsets.UTF_8));
        }

    }

    @Override
    public int getItemCount() {
        return Chapters.size();
    }

    public static class ContentsViewHolder extends RecyclerView.ViewHolder{
        public TextView content;

        public ContentsViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
        }
    }

    public ArrayList<Chapter> getChapters() {
        return Chapters;
    }

    public void setChapters(ArrayList<Chapter> chapters) {
        Chapters = chapters;
    }
}
