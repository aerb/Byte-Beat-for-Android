package com.tasty.fish.views;

public interface IParameterView {


    public interface IParameterViewListener
    {
        void OnParameterChanged(int index, int value);
        void OnTimeScaleChanged(double value);

        void OnResetParameters();
        void OnResetTime();
    }

    void registerIParameterViewListener(IParameterViewListener listener);

    public void setTimescale(double value);
    void setParameter(int id, int speed);
    void update();
}
