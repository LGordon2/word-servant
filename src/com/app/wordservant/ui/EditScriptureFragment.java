package com.app.wordservant.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.app.wordservant.R;

public class EditScriptureFragment extends SherlockFragment{

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(true);
	}
	
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.activity_input_scripture_manual, menu);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.activity_input_scripture_manual, null);
	}

	public void onViewCreated(View view, Bundle savedInstanceState){
		super.onViewCreated(view, savedInstanceState);
	}




}
