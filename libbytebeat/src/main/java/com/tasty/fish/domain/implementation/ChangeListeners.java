package com.tasty.fish.domain.implementation;

import com.tasty.fish.domain.IChangeListener;

import java.util.ArrayList;

public class ChangeListeners<T> {
    private final ArrayList<IChangeListener<T>> _listeners;

    public ChangeListeners() {
        _listeners = new ArrayList<IChangeListener<T>>();
    }

    public void add(IChangeListener<T> listener) {
        _listeners.add(listener);
    }

    public void notify(T element){
        for(IChangeListener<T> l : _listeners)
            l.onEvent(element);
    }
}
