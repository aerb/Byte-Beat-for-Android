package com.tasty.fish.domain.implementation;

import com.tasty.fish.domain.IExpressionsRepository;

import java.util.ArrayList;
import java.util.List;

public class ExpressionsRepository implements IExpressionsRepository {

    private final ArrayList<ByteBeatExpression> _expressions;
    private ByteBeatExpression _active;
    private final ArrayList<IExpressionsRepositoryListener> _listeners;

    public ExpressionsRepository() {
        _expressions = new ArrayList<ByteBeatExpression>();
        _listeners = new ArrayList<IExpressionsRepositoryListener>();
        initializeExpressions();
    }

    private void initializeExpressions(){
        for(ByteBeatExpression e : DefaultExpressions.get())
            addNewExpression(e);

        setActiveExpression(0);
    }

    public void addNewExpression(ByteBeatExpression e) {
        _expressions.add(e);
    }

    @Override
    public void setActiveExpressionLast() {
        setActiveExpression(_expressions.size() - 1);
    }

    @Override
    public void setIExpressionsRepositoryListener(IExpressionsRepositoryListener listener) {
        _listeners.add(listener);
    }

    @Override
    public List<ByteBeatExpression> getExpressions() {
        return _expressions;
    }

    public ByteBeatExpression getActive() {
        return _active;
    }

    @Override
    public void setActiveExpression(int position) {
        _active = _expressions.get(position);
        for(IExpressionsRepositoryListener l: _listeners)
            l.OnActiveExpressionChanged();
    }
}
