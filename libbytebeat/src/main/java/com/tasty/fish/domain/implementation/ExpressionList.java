package com.tasty.fish.domain.implementation;

import com.tasty.fish.domain.IExpressionList;
import com.tasty.fish.domain.Listener;

import java.util.ArrayList;
import java.util.List;

public class ExpressionList implements IExpressionList {

    private final ArrayList<Expression> _expressions;

    private final ListenerSet<Expression> _activeChange = new ListenerSet<Expression>();
    private final ListenerSet<Expression> _itemChange = new ListenerSet<Expression>();
    private final ListenerSet<List<Expression>> _setChange = new ListenerSet<List<Expression>>();

    private Listener<Expression> eventRouter = new Listener<Expression>() {
        @Override
        public void onEvent(Expression expression) {
            _itemChange.notify(expression);
        }
    };

    private Expression _active;

    public ExpressionList() {
        _expressions = new ArrayList<Expression>();
        initialize();
    }

    private void initialize(){
        for(Expression e : DefaultExpressions.get())
            add(e);
        setActive(0);
    }

    public void add(Expression expression) {
        _expressions.add(expression);
        expression.setChangeListener(eventRouter);
        _setChange.notify(_expressions);
    }

    @Override
    public void setActive(Expression expression) {
        int index = _expressions.indexOf(expression);
        if(index >= 0) setActive(index);
    }

    @Override
    public boolean contains(String name) {
        for (Expression e : _expressions){
            if(e.getName().equals(name)) return true;
        }
        return false;
    }

    @Override
    public boolean hasDirty() {
        for(Expression expression : _expressions){
            if(expression.isDirty() && !expression.isReadOnly())
                return true;
        }
        return false;
    }

    @Override
    public void addActiveChangedListener(Listener<Expression> listener) {
        _activeChange.add(listener);
    }

    @Override
    public void addExpressionUpdateListener(Listener<Expression> listener) {
        _itemChange.add(listener);
    }

    @Override
    public void addDataSetChangedListener(Listener<List<Expression>> listener) {
        _setChange.add(listener);
    }

    @Override
    public List<Expression> getExpressions() {
        return _expressions;
    }

    public Expression getActive() {
        return _active;
    }

    @Override
    public void setActive(int position) {
        _active = _expressions.get(position);
        _activeChange.notify(_active);
    }

    @Override
    public void remove(Expression expression) {
        if(_expressions.remove(expression))
            _setChange.notify(_expressions);
    }
}
