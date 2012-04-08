package com.tasty.fish;

import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class KeyboardHandler implements OnClickListener{
	View _view;
	public KeyboardHandler(View view){
		_view = view;
		registerButtonListeners(_view);
	}
	
	public void registerButtonListeners(View view){
		if (view instanceof Button){
			((Button)view).setOnClickListener(this);
			return;
		}

		for(int i = 0; i < ((ViewGroup)view).getChildCount(); ++i){
			View v = ((ViewGroup)view).getChildAt(i);
			registerButtonListeners(v);
		}
	}
	
	@Override
	public void onClick(View v) {
		
		
	}
	
}
