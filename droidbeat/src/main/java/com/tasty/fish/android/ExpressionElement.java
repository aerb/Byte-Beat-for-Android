package com.tasty.fish.android;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
        final int buttonLayoutId = 100;

        int rowHeight = (int)PixelConverter.ToPixels(50);

        setBackgroundColor(0xFF333333);
        AbsListView.LayoutParams
        elementLayoutParams = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, rowHeight);
        setLayoutParams(elementLayoutParams);
        HighlightLayout
            textLayout = new HighlightLayout(c);
            textLayout.setGravity(CENTER_VERTICAL);
            textLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println(_expression.getName());
                }
            });
            RelativeLayout.LayoutParams
            textLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            textLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            textLayoutParams.addRule(RelativeLayout.LEFT_OF, buttonLayoutId);
            textLayout.setLayoutParams(textLayoutParams);
            TextView
                text = new TextView(c);
                text.setGravity(CENTER_VERTICAL);
                text.setTextColor(Color.WHITE);
                text.setTextSize(20);
                text.setText(_expression.getName());
            textLayout.addView(text);
        addView(textLayout);
        LinearLayout
            buttonLayout = new LinearLayout(c);
            buttonLayout.setId(buttonLayoutId);
            buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
            RelativeLayout.LayoutParams
            buttonLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            buttonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            buttonLayout.setLayoutParams(buttonLayoutParams);
            HighlightButton
                edit = new HighlightButton(c);
                edit.setImageResource(R.drawable.save);
                edit.setLayoutParams(new ViewGroup.LayoutParams(rowHeight,rowHeight));
                buttonLayout.addView(edit);
            HighlightButton
                delete = new HighlightButton(c);
                delete.setImageResource(R.drawable.trash);
                delete.setLayoutParams(new ViewGroup.LayoutParams(rowHeight,rowHeight));
            buttonLayout.addView(delete);
        addView(buttonLayout);
    }
}