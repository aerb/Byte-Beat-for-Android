package com.tasty.fish.utils;

import java.util.ArrayList;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.tasty.fish.domain.ByteBeatExpression;

public class ExpressionAdapter implements SpinnerAdapter{

	private ArrayList<ByteBeatExpression> _array;

	public ExpressionAdapter(ArrayList<ByteBeatExpression> array){
		_array = array;
	}
	
	@Override
	public int getCount() {
		return _array.size();
	}

	@Override
	public Object getItem(int position) {
		return _array.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		return android.R.layout.simple_spinner_dropdown_item;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		  return null;
	}

	@Override
	public int getViewTypeCount() {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmpty() {
		return _array.size() == 0;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
	}

	@Override
	public View getDropDownView(int position, View convertView,
			ViewGroup parent) {
		 return this.getView(position, convertView, parent);
	}
}
