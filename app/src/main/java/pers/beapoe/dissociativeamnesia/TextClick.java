package pers.beapoe.dissociativeamnesia;

import android.app.Activity;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class TextClick extends ClickableSpan {
    private Activity activity;
    private int n_chapter = 0;
    private int n_span = 0;

    public TextClick(Activity activity,int n_chapter,int n_span){
        this.activity = activity;
        this.n_chapter = n_chapter;
        this.n_span = n_span;
    }
    @Override
    public void onClick(@NonNull View widget) {
        CustomApplication app = (CustomApplication) activity.getApplication();
        ArrayList<Chapter> chapters = app.getChapters();
        Chapter chapter = chapters.get(n_chapter);
        ArrayList<Span> spans = chapter.getSpans();
        Span span = spans.get(n_span);
        switch(span.getType()){
            case REPLACE:
                if(!span.isClicked()){
                    TextView Read = activity.findViewById(R.id.Read);
                    SpannableStringBuilder text = chapter.getContent();
                    text.replace(span.getStart(),span.getEnd(),span.getReplacement());
                    span.setColor(new ForegroundColorSpan(Color.parseColor("#FF6666")));
                    span.setClicked(true);
                    text.setSpan(span.getColor(),span.getStart(),span.getStart()+span.getReplacement().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spans.set(n_span,span);
                    chapter.setSpans(spans);
                    chapter.setContent(text);
                    chapters.set(n_chapter,chapter);
                    app.setChapters(chapters);
                    Read.setText(text);
                    break;
                }
        }
    }
}
