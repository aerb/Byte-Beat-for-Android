package com.tasty.fish.android.fragments.naming;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;
import com.tasty.fish.R;
import com.tasty.fish.android.DroidBeatActivity;
import com.tasty.fish.presenters.CreateExpressionPresenter;
import com.tasty.fish.views.ICreateExpressionView;

public class CreateExpressionFragment extends DialogFragment implements
        DialogInterface.OnClickListener,
        ICreateExpressionView
{
    private View _view;
    private EditText _name;
    private CreateExpressionPresenter _presenter;

    public static final String CopyArguement = "Copy";
    private boolean _copy;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        _view = getActivity().getLayoutInflater().inflate(R.layout.name_dialog, null);
        _name = (EditText)_view.findViewById(R.id.namingTextBox);
        _copy = getArguments() != null ? getArguments().getBoolean(CopyArguement) : false;

        _presenter = ((DroidBeatActivity)getActivity())
                .getCompositionRoot()
                .getCreateExpressionPresenter();
        _presenter.setView(this);
        _presenter.requestDefaultName(_copy);

        return new AlertDialog.Builder(getActivity())
                .setTitle("Define Name")
                .setView(_view)
                .setPositiveButton("Okee", this)
                .setNegativeButton("Cancel", this)
                .create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if(i == DialogInterface.BUTTON_POSITIVE){
            _presenter.addNewExpression(_name.getText().toString(), _copy);
        }
    }

    @Override
    public void setDefaultName(String name) {
        _name.setText(name);
    }
}
