package com.tasty.fish.android.fragments.visuals.expression;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.tasty.fish.android.DroidBeatActivity;
import com.tasty.fish.R;
import com.tasty.fish.presenters.ExpressionPresenter;
import com.tasty.fish.views.IExpressionView;

import java.util.ArrayList;

public class ExpressionFragment extends Fragment implements
        IExpressionView,
        View.OnClickListener
{

    private UnderlineSpan _underlineSpan = new UnderlineSpan();
    private ExpressionPresenter _presenter;
    private TextView _expressionTextView;
    private ArrayList<IExpressionViewListener> _listeners;
    private ImageView _editExpressionBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        _listeners = new ArrayList<IExpressionViewListener>();

        View layout = inflater.inflate(R.layout.expression_layout, null);
        _expressionTextView = (TextView)layout.findViewById(R.id.expressionTextView);
        _editExpressionBtn = (ImageView)layout.findViewById(R.id.editExpressionBtn);
        _editExpressionBtn.setOnClickListener(this);

        _presenter =
            ((DroidBeatActivity)getActivity())
            .getCompositionRoot()
            .getExpressionPresenter();
        _presenter.setView(this);

        return layout;
    }

    //region IExpressionView methods
    @Override
    public void setIExpressionViewListener(IExpressionViewListener listener) {
        _listeners.add(listener);
    }

    @Override
    public void setExpression(String s, int cursor) {
        SpannableString content = new SpannableString(s);
        content.removeSpan(_underlineSpan);
        content.setSpan(_underlineSpan, cursor, cursor + 1, 0);
        _expressionTextView.setText(content);
    }
    //endregion

    //region View.OnClickListener methods
    @Override
    public void onClick(View view) {
        for(IExpressionViewListener l : _listeners)
            l.OnRequestEdit();
    }
    //endregion
}
