package com.app.wordservant;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Settings extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
        Button myButton = (Button) findViewById(R.id.button1);
        myButton.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				displayToast();
			}

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
	private void displayToast() {
		this.finish();
	}
}
