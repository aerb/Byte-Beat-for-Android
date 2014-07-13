package com.tasty.fish.domain.implementation;

import com.tasty.fish.domain.IChangeListener;
import com.tasty.fish.domain.IExpressionsRepository;

import java.util.ArrayList;
import java.util.List;

public class ExpressionsRepository implements IExpressionsRepository {

    private final ArrayList<ByteBeatExpression> _expressions;

    private final ChangeListeners<ByteBeatExpression> _activeChange = new ChangeListeners<ByteBeatExpression>();
    private final ChangeListeners<ByteBeatExpression> _itemChange = new ChangeListeners<ByteBeatExpression>();
    private final ChangeListeners<List<ByteBeatExpression>> _setChange = new ChangeListeners<List<ByteBeatExpression>>();

    private IChangeListener<ByteBeatExpression> eventRouter = new IChangeListener<ByteBeatExpression>() {
        @Override
        public void onEvent(ByteBeatExpression expression) {
            _itemChange.notify(expression);
        }
    };

    private ByteBeatExpression _active;


    public ExpressionsRepository() {
        _expressions = new ArrayList<ByteBeatExpression>();
        initializeExpressions();
    }

    private void initializeExpressions(){
        for(ByteBeatExpression e : DefaultExpressions.get())
            add(e);
        setActiveExpression(0);
    }

    public void add(ByteBeatExpression e) {
        _expressions.add(e);
        e.setChangeListener(eventRouter);
        _setChange.notify(_expressions);
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
    public void addActiveChangedListener(IChangeListener<ByteBeatExpression> listener) {
        _activeChange.add(listener);
    }

    @Override
    public void addExpressionUpdateListener(IChangeListener<ByteBeatExpression> listener) {
        _itemChange.add(listener);
    }

    @Override
    public void addDataSetChangedListener(IChangeListener<List<ByteBeatExpression>> listener) {
        _setChange.add(listener);
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
        _activeChange.notify(_active);
    }

    @Override
    public void remove(ByteBeatExpression expression) {
        if(_expressions.remove(expression))
            _setChange.notify(_expressions);
    }
}
