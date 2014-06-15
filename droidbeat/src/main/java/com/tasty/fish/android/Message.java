package com.tasty.fish.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

public class Message {
    private static Context getContext(){
        return DroidBeatApplication.instance.getApplicationContext();
    }

    public static void std(String s){
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    public static void err(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
    }

    public static void confirm(Context c, String message, DialogInterface.OnClickListener confirm) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder
            .setMessage(message)
            .setPositiveButton("Yes", confirm)
            .setNegativeButton("No", null)
            .show();
    }
}
