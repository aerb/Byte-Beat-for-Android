package com.tasty.fish.views;

public interface IBufferView {
    public interface IBufferViewListener
    {
    }

    void registerIBufferViewListener(IBufferView listener);
    void setDisplayBuffer(byte[] samples, int t);
    void setTime(int time);
}
