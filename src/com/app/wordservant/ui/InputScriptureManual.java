package com.app.wordservant.ui;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.app.wordservant.R;

public class InputScriptureManual extends SherlockFragmentActivity {
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_fragment_holder);
		InputScriptureFragment inputScriptureFragment = new InputScriptureFragment();
		getSupportFragmentManager().beginTransaction().add(R.id.fragmentHolder, inputScriptureFragment).commit();
	}

}
