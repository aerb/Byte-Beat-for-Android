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
import com.tasty.fish.android.IExpressionEventListener;
import com.tasty.fish.android.Message;
import com.tasty.fish.domain.IChangeListener;
import com.tasty.fish.domain.IExpressionsRepository;
import com.tasty.fish.domain.implementation.ByteBeatExpression;
import com.tasty.fish.presenters.ExpressionSelectionPresenter;

import java.io.IOException;
import java.util.List;

public class ExpressionSelectionFragment extends Fragment {
    private ExpressionSelectionPresenter _presenter;
    private ImageView _cancelBtn;
    private ListView _list;
    private ExpressionSelectionAdapter _adapter;
    private IExpressionsRepository _repo;

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
        _adapter = new ExpressionSelectionAdapter(getActivity(), _repo.getExpressions());
        _list.setAdapter(_adapter);
        _adapter.setSaveListener(new IExpressionEventListener() {
            @Override
            public void onEvent(ByteBeatExpression expression) {
            try {
                _presenter.save(expression);
                Message.std("Saved " + expression.getName());
            } catch (IOException e) {
                Message.err("Could not save " + expression.getName());
            }
            }
        });
        _adapter.setDeleteListener(new IExpressionEventListener() {
            @Override
            public void onEvent(final ByteBeatExpression expression) {
            Message.confirm(
                getActivity(),
                "Are you sure you want to delete " + expression.getName() + "?",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            _presenter.delete(expression);
                            Message.std("Deleted " + expression.getName());
                        } catch (IOException e) {
                            Message.err("Could not delete " + expression.getName());
                        }
                    }
                });
            }
        });
        _adapter.setSelectListener(new IExpressionEventListener() {
            @Override
            public void onEvent(ByteBeatExpression expression) {
            _presenter.selectExpression(expression);
            }
        });

        _cancelBtn = (ImageView)view.findViewById(R.id.selectionCancel);
        _cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _presenter.exitView();
            }
        });

        _repo.addDataSetChangedListener(new IChangeListener<List<ByteBeatExpression>>() {
            @Override
            public void onEvent(List<ByteBeatExpression> expression) {
                _list.invalidateViews();
            }
        });
        _repo.addExpressionUpdateListener(new IChangeListener<ByteBeatExpression>() {
            @Override
            public void onEvent(ByteBeatExpression expression) {
                int index = _repo.getExpressions().indexOf(expression);
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
