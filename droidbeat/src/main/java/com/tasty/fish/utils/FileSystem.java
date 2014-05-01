package com.tasty.fish.utils;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

public class FileSystem {

    private String mkDir(String f0, String f1) throws IOException {
        File f2 = new File(f0,f1);
        if(!f2.exists())
            if(!f2.mkdir())
                throw new IOException("Could not create dir " + f2);
        return f2.toString();
    }

    public String getNextExportName() throws IOException {
        String dir = getExportsDirectory();
        int attempt = 0;
        while(true) {
            File newExportPath = new File(dir, "droidbeat_export_" + attempt + ".wav");
            if(!newExportPath.exists())
                return newExportPath.toString();
            attempt++;
        }
    }

    public String getExportsDirectory() throws IOException {
        return mkDir(getPublicDroidBeatDirectory(), "Exports");
    }

    public String getPublicDroidBeatDirectory() throws IOException {
        String state = Environment.getExternalStorageState();
        if(!state.equals(Environment.MEDIA_MOUNTED))
            throw new IOException("Cannot access external storage.");

        String publicDir = Environment.getExternalStorageDirectory().toString();
        return mkDir(publicDir, "DroidBeat");
    }
}
