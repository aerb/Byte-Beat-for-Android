package com.tasty.fish.interfaces;

public interface IDroidBeatView
{
    public interface IDroidBeatViewListener
    {
        void OnExpressionChanged(String name);
        void OnArgumentChanged(int index, double value);
        void OnTimeScaleChanged(double value);
    }

    void registerIDroidBeatViewListener(IDroidBeatViewListener listener);

    void displayBuffer(byte[] samples, int t);
    void updateT(int time);
    void postInvalidate();
    public void updateSeekerSpeedPostion(double value);
    void updateSeekerPostion(int id, double speed);
    void updateDisplayedExpression(String s, int cursor);
}
