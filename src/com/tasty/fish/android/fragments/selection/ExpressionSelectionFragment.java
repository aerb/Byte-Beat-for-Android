package com.tasty.fish.android.fragments.selection;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import com.tasty.fish.android.DroidBeatActivity;
import com.tasty.fish.android.DroidBeatApplication;
import com.tasty.fish.domain.implementation.ByteBeatExpression;
import com.tasty.fish.presenters.ExpressionSelectionPresenter;
import com.tasty.fish.views.IExpressionSelectionView;

import java.util.ArrayList;
import java.util.List;

public class ExpressionSelectionFragment extends ListFragment implements IExpressionSelectionView {
    private List<ByteBeatExpression> _expressions;
    private ExpressionSelectionPresenter _presenter;
    private ArrayList<IExpressionSelectionViewListener> _listeners;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        _listeners = new ArrayList<IExpressionSelectionViewListener>();
        _presenter =
                ((DroidBeatActivity)activity)
                .getCompositionRoot()
                .getExpressionSelectorPresenter();
        _presenter.setView(this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        for(IExpressionSelectionViewListener listener : _listeners)
            listener.OnExpressionSelected(position);
        super.onListItemClick(l, v, position, id);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setListAdapter(new ExpressionSelectionAdapter(getActivity().getLayoutInflater(), _expressions));
    }

    @Override
    public void addIExpressionSelectionViewListener(IExpressionSelectionViewListener listener) {
        _listeners.add(listener);
    }

    @Override
    public void setDataSource(List<ByteBeatExpression> expressions) {
        _expressions = expressions;
    }
}
