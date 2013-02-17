package com.tasty.fish;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import com.tasty.fish.domain.ByteBeatExpression;

import java.util.ArrayList;
import java.util.List;

public class ExpressionSelectionView extends ListFragment implements IExpressionSelectionView {
    private List<ByteBeatExpression> _expressions;
    private ExpressionSelectionPresenter _presenter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        _presenter =  ((DroidBeatApplication)activity.getApplication()).getExpressionSelectorPresenter();
        _presenter.setView(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(new ExpressionSelectionAdapter(getActivity().getLayoutInflater(), _expressions));
    }

    @Override
    public void setDataSource(List<ByteBeatExpression> expressions) {
        _expressions = expressions;
    }
}
