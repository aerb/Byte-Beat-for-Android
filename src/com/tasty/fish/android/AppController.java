package com.tasty.fish.android;
import com.tasty.fish.views.IAppController;

public class AppController implements IAppController{

    private DroidBeatActivity _activity;

    public void setActivity(DroidBeatActivity activity){
        _activity = activity;
   }

    @Override
    public void ShowKeyboard() {
        if(_activity != null)
            _activity.ShowKeyboard();
    }

    @Override
    public void ShowSelector() {
        if(_activity != null)
            _activity.ShowSelector();
    }

    @Override
    public void ShowParams() {
        if(_activity != null)
            _activity.ShowParams();
    }

    @Override
    public void CloseSelector() {
        ShowParams();
    }

    @Override
    public void CloseKeyboard() {
        ShowParams();
    }
}
