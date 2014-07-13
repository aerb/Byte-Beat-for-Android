package com.tasty.fish.domain;

public interface IChangeListener<T>
{
    void onEvent(T expression);
}
