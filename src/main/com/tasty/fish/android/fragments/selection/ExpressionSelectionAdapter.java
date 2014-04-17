package com.tasty.fish.android.fragments.selection;

import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.tasty.fish.R;
import com.tasty.fish.domain.implementation.ByteBeatExpression;

import java.util.List;

public class ExpressionSelectionAdapter implements ListAdapter {

    private final List<ByteBeatExpression> _expressions;
    private final LayoutInflater _inflater;

    public ExpressionSelectionAdapter(LayoutInflater inflater, List<ByteBeatExpression> expressions) {
        _expressions = expressions;
        _inflater = inflater;
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
        View row = _inflater.inflate(R.layout.expression_selector_row, null);
        TextView text = (TextView)row.findViewById(R.id.expressionSelectorRowText);
        text.setText(_expressions.get(i).getName());
        return row;
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
}
