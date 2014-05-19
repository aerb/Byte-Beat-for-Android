package com.tasty.fish.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class Manifest {
    public static boolean isPaidVersion(Context context) throws PackageManager.NameNotFoundException {
        ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        if(ai == null || ai.metaData == null)
            throw new PackageManager.NameNotFoundException();

        String appVersion = (String)ai.metaData.get("AppVersion");
        return appVersion.equals("paid");
    }
}
