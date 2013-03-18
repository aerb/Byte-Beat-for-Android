package com.tasty.fish.views;

public interface IDroidBeatView
{
    public interface IDroidBeatViewListener
    {
        void OnStartPlay();
        void OnStopPlay();
    }

    void registerIDroidBeatViewListener(IDroidBeatViewListener listener);
    void setTitle(String title);
}
