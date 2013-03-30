package com.tasty.fish.android.fragments.selection;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.tasty.fish.R;
import com.tasty.fish.android.DroidBeatActivity;
import com.tasty.fish.domain.implementation.ByteBeatExpression;
import com.tasty.fish.presenters.ExpressionSelectionPresenter;
import com.tasty.fish.views.IExpressionSelectionView;

import java.util.ArrayList;
import java.util.List;

public class ExpressionSelectionFragment extends Fragment implements IExpressionSelectionView, AdapterView.OnItemClickListener {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.expression_selector, container, false);
        ListView list = (ListView)view.findViewById(R.id.listView);
        list.setAdapter(new ExpressionSelectionAdapter(getActivity().getLayoutInflater(), _expressions));
        list.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void addIExpressionSelectionViewListener(IExpressionSelectionViewListener listener) {
        _listeners.add(listener);
    }

    @Override
    public void setDataSource(List<ByteBeatExpression> expressions) {
        _expressions = expressions;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        for(IExpressionSelectionViewListener listener : _listeners)
            listener.OnExpressionSelected(i);
    }
}
