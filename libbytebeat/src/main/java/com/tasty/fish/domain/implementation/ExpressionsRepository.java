package com.tasty.fish.domain.implementation;

import com.tasty.fish.domain.IExpressionListener;
import com.tasty.fish.domain.IExpressionsRepository;

import java.util.ArrayList;
import java.util.List;

public class ExpressionsRepository implements IExpressionsRepository {

    private final ArrayList<ByteBeatExpression> _expressions;
    private ByteBeatExpression _active;
    private final ArrayList<IExpressionListener> _listeners;

    public ExpressionsRepository() {
        _expressions = new ArrayList<ByteBeatExpression>();
        _listeners = new ArrayList<IExpressionListener>();
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
    public void setActiveExpression(ByteBeatExpression expression) {
        int index = _expressions.indexOf(expression);
        if(index >= 0) setActiveExpression(index);
    }

    @Override
    public boolean contains(String name) {
        for (ByteBeatExpression e : _expressions){
            if(e.getName().equals(name)) return true;
        }
        return false;
    }

    @Override
    public void setActiveChangedListener(IExpressionListener listener) {
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
        for(IExpressionListener l: _listeners)
            l.onExpressionEvent();
    }

    @Override
    public void updateActive(String text) {
        getActive().setExpressionString(text);
        for(IExpressionListener listener : _listeners)
            listener.onExpressionEvent();
    }
}
