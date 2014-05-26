package com.tasty.fish.android;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class HighlightLayout extends LinearLayout{

    public HighlightLayout(Context context) {
        super(context);
        init();
    }

    public HighlightLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private static int haloLight = 0xff33b5e5;

    private void init(){
        StateListDrawable background = new StateListDrawable();
        background.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(haloLight));
        background.addState(new int[]{}, new ColorDrawable(Color.TRANSPARENT));
        setBackgroundDrawable(background);
    }
}
