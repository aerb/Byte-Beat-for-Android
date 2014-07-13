package com.tasty.fish.android.fragments.selection;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.tasty.fish.android.ExpressionElement;
import com.tasty.fish.android.IExpressionEventListener;
import com.tasty.fish.domain.implementation.ByteBeatExpression;

import java.util.List;

public class ExpressionSelectionAdapter implements ListAdapter {

    private final List<ByteBeatExpression> _expressions;
    private final Context _context;
    private IExpressionEventListener _onSave;
    private IExpressionEventListener _onDelete;
    private IExpressionEventListener _onSelect;

    public ExpressionSelectionAdapter(Context context, List<ByteBeatExpression> expressions) {
        _expressions = expressions;
        _context = context;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int i) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) { }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) { }

    @Override
    public int getCount() {
        return _expressions.size();
    }

    @Override
    public Object getItem(int i) {
        return _expressions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ExpressionElement cell = (ExpressionElement) view;
        if(view == null) {
            cell = new ExpressionElement(_context);
            cell.setSelectionListener(_onSelect);
            cell.setSaveListener(_onSave);
            cell.setDeleteListener(_onDelete);
        }
        updateView(i, cell);
        return cell;
    }

    public void updateView(int i, ExpressionElement view) {
        view.setExpression(_expressions.get(i));
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return _expressions.isEmpty();
    }


    public void setSaveListener(IExpressionEventListener listener) {
        this._onSave = listener;
    }
    public void setDeleteListener(IExpressionEventListener listener) {
        _onDelete = listener;
    }

    public void setSelectListener(IExpressionEventListener _onSelect) {
        this._onSelect = _onSelect;
    }
}
