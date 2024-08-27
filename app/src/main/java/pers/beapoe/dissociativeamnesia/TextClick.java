package pers.beapoe.dissociativeamnesia;

import android.app.Activity;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class TextClick extends ClickableSpan {
    private Activity activity;

    public TextClick(Activity activity){this.activity = activity;}
    @Override
    public void onClick(@NonNull View widget) {
        Toast.makeText(activity,"successful",Toast.LENGTH_SHORT).show();
    }
}
