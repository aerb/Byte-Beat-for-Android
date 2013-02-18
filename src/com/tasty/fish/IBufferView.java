package com.tasty.fish;

public interface IBufferView {
    public interface IBufferViewListener
    {
    }

    void registerIBufferViewListener(IBufferView listener);
    void setDisplayBuffer(byte[] samples, int t);
    void setTime(int time);
}
