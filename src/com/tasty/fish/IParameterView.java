package com.tasty.fish;

public interface IParameterView {
    public interface IParameterViewListener
    {
        void OnArgumentChanged(int index, double value);
        void OnTimeScaleChanged(double value);

        void OnResetArgs();
        void OnResetTime();
    }

    void registerIDroidBeatViewListener(IParameterViewListener listener);

    public void updateSeekerSpeedPostion(double value);
    void updateSeekerPostion(int id, double speed);
}
