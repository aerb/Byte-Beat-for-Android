package com.tasty.fish.android.fragments.naming;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tasty.fish.android.DroidBeatActivity;
import com.tasty.fish.presenters.CreateExpressionPresenter;
import com.tasty.fish.presenters.NamingResponse;

public class CreateExpressionFragment extends DialogFragment
{

    private EditText name;
    private String expression = "t";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final CreateExpressionPresenter presenter = ((DroidBeatActivity) getActivity())
            .getCompositionRoot()
            .getCreateExpressionPresenter();
        final Context c = getActivity();
        final AlertDialog dialog = new AlertDialog.Builder(c)
            .setTitle("Define Name")
            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    presenter.addNewExpression(name.getText().toString(), expression);
                }
            })
            .setNegativeButton("Cancel", null)
            .create();

            LinearLayout
                view = new LinearLayout(c);
                view.setOrientation(LinearLayout.VERTICAL);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                final TextView
                    errorText = new TextView(c);
                    errorText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    errorText.setTextColor(Color.RED);
                view.addView(errorText);
                    name = new EditText(c);
                    name.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    name.setText(presenter.getSuggestedName());
                    name.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                            NamingResponse response = presenter.isValidName(name.getText().toString());
                            Button btn = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                            if(btn == null) btn = new Button(c);
                            switch (response) {
                                case AlreadyExists:
                                    errorText.setText("Name already exists.");
                                    btn.setEnabled(false);
                                    break;
                                case InvalidChar:
                                    errorText.setText("Can only use alphanumeric characters and underscore.");
                                    btn.setEnabled(false);
                                    break;
                                case Valid:
                                    errorText.setText("");
                                    btn.setEnabled(true);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                view.addView(name);
            dialog.setView(view);
        return dialog;
    }

    public CreateExpressionFragment setExpressionToCreate(String expressionToCreate) {
        this.expression = expressionToCreate;
        return this;
    }
}
