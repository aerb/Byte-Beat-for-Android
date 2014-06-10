package com.tasty.fish.android.fragments.selection;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import com.tasty.fish.R;
import com.tasty.fish.android.DroidBeatActivity;
import com.tasty.fish.android.IExpressionEventListener;
import com.tasty.fish.android.Message;
import com.tasty.fish.domain.implementation.ByteBeatExpression;
import com.tasty.fish.presenters.ExpressionSelectionPresenter;
import com.tasty.fish.views.IExpressionSelectionView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExpressionSelectionFragment extends Fragment implements IExpressionSelectionView, AdapterView.OnItemClickListener, View.OnClickListener {
    private List<ByteBeatExpression> _expressions;
    private ExpressionSelectionPresenter _presenter;
    private ArrayList<IExpressionSelectionViewListener> _listeners;
    private ImageView _cancelBtn;
    private ListView _list;
    private ExpressionSelectionAdapter _adapter;

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
        _list = (ListView)view.findViewById(R.id.listView);
        _adapter = new ExpressionSelectionAdapter(getActivity(), _expressions);
        _list.setAdapter(_adapter);
        _list.setOnItemClickListener(this);
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

        _cancelBtn = (ImageView)view.findViewById(R.id.selectionCancel);
        _cancelBtn.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        if(view == _cancelBtn){
            for(IExpressionSelectionViewListener l : _listeners)
                l.OnCancelRequested();
        }
    }

    public void update(){
        _list.invalidateViews();
    }


}
