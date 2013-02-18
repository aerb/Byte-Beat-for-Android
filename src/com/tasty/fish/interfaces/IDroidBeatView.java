package com.tasty.fish.interfaces;

public interface IDroidBeatView
{
    public interface IDroidBeatViewListener
    {
        void OnStartPlay();
        void OnStopPlay();

        void OnExpressionChanged(int id);
    }

    void registerIDroidBeatViewListener(IDroidBeatViewListener listener);

    void setDisplayBuffer(byte[] samples, int t);
    void setTime(int time);
}
