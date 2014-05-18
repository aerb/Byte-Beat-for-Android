package com.tasty.fish.android.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

public class ExportFragment extends DialogFragment{

    private String _path;

    public void setSavedPath(String path){
        _path = path;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context c = getActivity();

        LinearLayout
            layout = new LinearLayout(c);
            layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            layout.setOrientation(LinearLayout.VERTICAL);
            TextView
                textView = new TextView(c);
                textView.setText(_path);
                textView.setTextSize(14);
                textView.setPadding(10,10,10,10);
                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layout.addView(textView);
            Button
                playBtn = new Button(c);
                playBtn.setText("Play");
                playBtn.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                playBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(new File(_path)), "audio/*");
                        startActivity(intent);
                    }
                });
            layout.addView(playBtn);

        return new AlertDialog.Builder(getActivity())
            .setTitle("Saved to")
            .setView(layout)
            .create();
    }
}