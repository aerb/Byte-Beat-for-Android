package com.tasty.fish.views;

public interface IParameterView {


    public interface IParameterViewListener
    {
        void OnParameterChanged(int index, int value);
        void OnTimeDeltaChanged(double value);

        void OnResetParameters();
        void OnResetTime();
    }

    void registerIParameterViewListener(IParameterViewListener listener);

    public void setTimeDelta(double value);
    void setParameter(int id, int speed);
    void update();
}
