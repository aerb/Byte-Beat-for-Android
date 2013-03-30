package com.tasty.fish.views;

public interface IBufferView {


    public interface IBufferViewListener
    {
    }

    void registerIBufferViewListener(IBufferView listener);
    void setDisplayBuffer(byte[] samples);
    void update();
}
