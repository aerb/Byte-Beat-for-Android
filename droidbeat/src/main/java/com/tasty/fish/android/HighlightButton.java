package com.tasty.fish.android;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class HighlightButton extends ImageButton{
    public HighlightButton(Context context) {
        super(context);
        init();
    }

    public HighlightButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HighlightButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private static int haloLight = 0xff33b5e5;

    private void init(){
        setScaleType(ScaleType.FIT_CENTER);
        StateListDrawable background = new StateListDrawable();
        background.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(haloLight));
        background.addState(new int[]{}, new ColorDrawable(Color.TRANSPARENT));
        setBackgroundDrawable(background);
    }

}
