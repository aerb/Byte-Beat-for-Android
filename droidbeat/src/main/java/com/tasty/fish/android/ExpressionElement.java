package com.tasty.fish.android;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
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
    private IExpressionEventListener _onSave;
    private IExpressionEventListener _onDelete;
    private IExpressionEventListener _selection;

    public ExpressionElement(Context context) {
        super(context);
        _context = context;
    }

    public void setExpression(ByteBeatExpression expression){
        _expression = expression;
    }

    public void create() {
        Context c = _context;
        final int buttonLayoutId = 100;

        int rowHeight = (int)PixelConverter.ToPixels(50);

        setBackgroundColor(0xFF333333);
        AbsListView.LayoutParams
        elementLayoutParams = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, rowHeight);
        setLayoutParams(elementLayoutParams);
        HighlightLayout
            textLayout = new HighlightLayout(c);
            textLayout.setGravity(Gravity.CENTER_VERTICAL);
            textLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    _selection.onEvent(_expression);
                }
            });
            RelativeLayout.LayoutParams
            textLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            textLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            textLayoutParams.addRule(RelativeLayout.LEFT_OF, buttonLayoutId);
            textLayout.setLayoutParams(textLayoutParams);
            TextView
                text = new TextView(c);
                text.setGravity(Gravity.CENTER_VERTICAL);
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
                save = new HighlightButton(c);
                if(_expression.isReadOnly()){
                    save.setAlpha(255/2);
                    save.setEnabled(false);
                }
                save.setImageResource(R.drawable.save);
                save.setLayoutParams(new ViewGroup.LayoutParams(rowHeight, rowHeight));
                save.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        _onSave.onEvent(_expression);
                    }
                });
                buttonLayout.addView(save);
            HighlightButton
                delete = new HighlightButton(c);
                if(_expression.isReadOnly()){
                    delete.setAlpha(255/2);
                    delete.setEnabled(false);
                }
                delete.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        _onDelete.onEvent(_expression);
                    }
                });
                delete.setImageResource(R.drawable.trash);
                delete.setLayoutParams(new ViewGroup.LayoutParams(rowHeight,rowHeight));

            buttonLayout.addView(delete);
        addView(buttonLayout);
    }



    public void setSelectionListener(IExpressionEventListener listener) {
        _selection = listener;
    }
    public void setSaveListener(IExpressionEventListener listener) {
        _onSave = listener;
    }
    public void setDeleteListener(IExpressionEventListener listener) {
        _onDelete = listener;
    }
}
