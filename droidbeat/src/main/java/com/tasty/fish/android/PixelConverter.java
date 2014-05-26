package com.tasty.fish.android;

import android.app.Application;
import android.content.res.Resources;
import android.util.TypedValue;

import java.security.InvalidParameterException;

public class PixelConverter {

    public static float ToPixels(float dp){
        Resources res = Resources.getSystem();
        if(res == null) throw new InvalidParameterException();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
    }
}
