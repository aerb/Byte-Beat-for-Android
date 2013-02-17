package com.tasty.fish.interfaces;

public interface IDroidBeatView
{
    public interface IDroidBeatViewListener
    {
        void OnStartPlay();
        void OnStopPlay();

        void OnExpressionChanged(int id);
        void OnArgumentChanged(int index, double value);
        void OnTimeScaleChanged(double value);

        void OnResetArgs();
        void OnResetTime();
    }

    void registerIDroidBeatViewListener(IDroidBeatViewListener listener);

    void setExpression(String s, int cursor);
    void setDisplayBuffer(byte[] samples, int t);
    void setTime(int time);

    public void updateSeekerSpeedPostion(double value);
    void updateSeekerPostion(int id, double speed);
}
