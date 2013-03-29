package com.tasty.fish.views;

public interface IMediaControlsView
{
    public interface IMediaControlsListener
    {
        void OnStartPlay();
        void OnStopPlay();
    }

    void registerIDroidBeatViewListener(IMediaControlsListener listener);
    void setTitle(String title);
}
