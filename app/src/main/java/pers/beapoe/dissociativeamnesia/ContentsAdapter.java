package pers.beapoe.dissociativeamnesia;

import android.app.Activity;
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

    public ContentsAdapter(Activity activity){
        Chapters = ((CustomApplication)activity.getApplication()).getChapters();
    }

    @NonNull
    @Override
    public ContentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content,parent,false);
        return new ContentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentsViewHolder holder, int position) {
        if(Chapters.get(position).isRead()){
            holder.content.setText(Chapters.get(position).getChapterName());
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
