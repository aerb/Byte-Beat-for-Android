package com.tasty.fish.domain.implementation;

import com.tasty.fish.domain.Listener;

import java.util.HashSet;

public class ListenerSet<T> {
    private final HashSet<Listener<T>> _listeners;

    public ListenerSet() {
        _listeners = new HashSet<Listener<T>>();
    }

    public void add(Listener<T> listener) {
        _listeners.add(listener);
    }

    public void notify(T element){
        for(Listener<T> l : _listeners)
            l.onEvent(element);
    }
}
