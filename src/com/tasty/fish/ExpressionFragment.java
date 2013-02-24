package com.tasty.fish;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created with IntelliJ IDEA.
 * User: ade
 * Date: 2/24/13
 * Time: 6:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExpressionFragment extends Fragment implements IExpressionView {

    private UnderlineSpan _underlineSpan = new UnderlineSpan();
    private ExpressionPresenter _presenter;
    private TextView _expressionTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.expression_layout, null);
        _expressionTextView = (TextView)layout.findViewById(R.id.expressionTextView);

        _presenter = ((DroidBeatView)getActivity()).getExpressionPresenter();
        _presenter.setExpressionView(this);

        return layout;
    }

    public void setExpression(String s, int cursor) {
        SpannableString content = new SpannableString(s);
        content.removeSpan(_underlineSpan);
        content.setSpan(_underlineSpan, cursor, cursor + 1, 0);
        _expressionTextView.setText(content);
    }
}
