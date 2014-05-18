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
import android.widget.RelativeLayout;
import android.widget.TableLayout;
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
            LinearLayout
                buttonsLayout = new LinearLayout(c);
                buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
                buttonsLayout.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                Button
                    playBtn = new Button(c);
                    playBtn.setText("Play");
                    playBtn.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                    playBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.fromFile(new File(_path)), "audio/*");
                            startActivity(intent);
                        }
                    });
                buttonsLayout.addView(playBtn);
                Button
                    exportBtn = new Button(c);
                    exportBtn.setText("Export");
                    exportBtn.setLayoutParams(new TableLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT, 1f));
                    exportBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(_path)));
                            intent.setType("audio/vnd.wave");
                            startActivity(intent);
                        }
                    });
                buttonsLayout.addView(exportBtn);
            layout.addView(buttonsLayout);

        return new AlertDialog.Builder(getActivity())
            .setTitle("Saved to")
            .setView(layout)
            .create();
    }
}