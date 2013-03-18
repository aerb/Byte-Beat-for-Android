package com.tasty.fish.views;

public interface IParameterView {
    public interface IParameterViewListener
    {
        void OnParameterChanged(int index, double value);
        void OnTimeScaleChanged(double value);

        void OnResetParameters();
        void OnResetTime();
    }

    void registerIDroidBeatViewListener(IParameterViewListener listener);

    public void setTimescale(double value);
    void setParameter(int id, double speed);
}
