package com.app.wordservant.ui;

import com.app.wordservant.R;
import com.app.wordservant.R.id;
import com.app.wordservant.R.layout;
import com.app.wordservant.R.menu;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class InputScriptureManual extends FragmentActivity {
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_fragment_holder);
		InputScriptureFragment inputScriptureFragment = new InputScriptureFragment();
		getSupportFragmentManager().beginTransaction().add(R.id.fragmentHolder, inputScriptureFragment).commit();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_input_scripture_manual, menu);
        return true;
    }
}