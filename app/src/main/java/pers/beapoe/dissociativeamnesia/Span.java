package pers.beapoe.dissociativeamnesia;

import android.graphics.Color;
import android.text.style.ForegroundColorSpan;

public class Span {
    private int start = 0;
    private int end = 0;
    private String Replacement = "";
    private ForegroundColorSpan color = new ForegroundColorSpan(Color.parseColor("#66B2FF"));
    private CustomApplication.SpanType type;

    public Span(){}

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getReplacement() {
        return Replacement;
    }

    public void setReplacement(String replacement) {
        Replacement = replacement;
    }

    public ForegroundColorSpan getColor() {
        return color;
    }

    public CustomApplication.SpanType getType() {
        return type;
    }

    public void setType(CustomApplication.SpanType type) {
        this.type = type;
    }
}
