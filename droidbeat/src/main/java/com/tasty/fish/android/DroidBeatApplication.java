package com.tasty.fish.android;

import android.app.Application;

public class DroidBeatApplication extends Application {

    public static DroidBeatApplication instance;

    public DroidBeatApplication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
}
