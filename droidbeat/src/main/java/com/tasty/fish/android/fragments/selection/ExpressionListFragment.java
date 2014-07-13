package com.tasty.fish.android.fragments.selection;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.tasty.fish.R;
import com.tasty.fish.android.DroidBeatActivity;
import com.tasty.fish.android.ExpressionElement;
import com.tasty.fish.android.Message;
import com.tasty.fish.domain.IExpressionList;
import com.tasty.fish.domain.Listener;
import com.tasty.fish.domain.implementation.Expression;
import com.tasty.fish.presenters.ExpressionSelectionPresenter;

import java.io.IOException;
import java.util.List;

public class ExpressionListFragment extends Fragment {
    private ExpressionSelectionPresenter _presenter;
    private ListView _list;
    private ExpressionListAdapter _adapter;
    private IExpressionList _repo;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        _presenter =
                ((DroidBeatActivity)activity)
                .getCompositionRoot()
                .getExpressionSelectorPresenter();
        _repo =
            ((DroidBeatActivity)activity)
            .getCompositionRoot()
            .getExpressionsRepository();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.expression_selector, container, false);
        _list = (ListView)view.findViewById(R.id.listView);
        _adapter = new ExpressionListAdapter(getActivity(), _repo.getExpressions());
        _list.setAdapter(_adapter);
        _adapter.setSaveListener(new Listener<Expression>() {
            @Override
            public void onEvent(Expression item) {
            try {
                _presenter.save(item);
                Message.std("Saved " + item.getName());
            } catch (IOException e) {
                Message.err("Could not save " + item.getName());
            }
            }
        });
        _adapter.setDeleteListener(new Listener<Expression>() {
            @Override
            public void onEvent(final Expression item) {
            Message.confirm(
                getActivity(),
                "Are you sure you want to delete " + item.getName() + "?",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            _presenter.delete(item);
                            Message.std("Deleted " + item.getName());
                        } catch (IOException e) {
                            Message.err("Could not delete " + item.getName());
                        }
                    }
                });
            }
        });
        _adapter.setSelectListener(new Listener<Expression>() {
            @Override
            public void onEvent(Expression item) {
            _presenter.selectExpression(item);
            }
        });

        ImageView _cancelBtn = (ImageView) view.findViewById(R.id.selectionCancel);
        _cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _presenter.exitView();
            }
        });

        _repo.addDataSetChangedListener(new Listener<List<Expression>>() {
            @Override
            public void onEvent(List<Expression> item) {
                _list.invalidateViews();
            }
        });
        _repo.addExpressionUpdateListener(new Listener<Expression>() {
            @Override
            public void onEvent(Expression item) {
                int index = _repo.getExpressions().indexOf(item);
                updateChild(index);
            }
        });
        return view;
    }

    private void updateChild(int index){
        View child = _list.getChildAt(index - _list.getFirstVisiblePosition());
        if(child != null)
            _adapter.updateView(index, (ExpressionElement) child);
    }
}
