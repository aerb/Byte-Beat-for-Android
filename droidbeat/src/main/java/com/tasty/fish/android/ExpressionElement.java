package com.tasty.fish.android;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tasty.fish.R;
import com.tasty.fish.domain.implementation.ByteBeatExpression;

public class ExpressionElement extends RelativeLayout {
    private final Context _context;
    private ByteBeatExpression _expression;

    public ExpressionElement(Context context) {
        super(context);
        _context = context;
    }

    public void setExpression(ByteBeatExpression expression){
        _expression = expression;
        init(_context);
    }

    private void init(Context c){
        setBackgroundColor(0xFFC83D);
        TextView
            text = new TextView(c);
            text.setTextColor(Color.WHITE);
            text.setText(_expression.getName());
        addView(text);
        LinearLayout
            layout = new LinearLayout(c);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            RelativeLayout.LayoutParams
                params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            layout.setLayoutParams(params);
            int buttonSize = (int)PixelConverter.ToPixels(50);
            HighlightButton
                edit = new HighlightButton(c);
                edit.setImageResource(R.drawable.save);

                edit.setLayoutParams(new ViewGroup.LayoutParams(buttonSize,buttonSize));
                layout.addView(edit);
            HighlightButton
                delete = new HighlightButton(c);
                delete.setImageResource(R.drawable.trash);
                delete.setLayoutParams(new ViewGroup.LayoutParams(buttonSize,buttonSize));
            layout.addView(delete);
        addView(layout);
    }



}
