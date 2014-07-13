package com.tasty.fish.domain;

public interface Listener<T>
{
    void onEvent(T expression);
}
