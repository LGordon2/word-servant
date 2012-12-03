package com.app.wordservant;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ViewSwitcher;

public class TagPreview extends Activity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flashcard_layout);
		final ViewSwitcher vSwitcher = (ViewSwitcher) findViewById(R.id.cardSwitcher);
		Button button = (Button) findViewById(R.id.flipCardButton);
		vSwitcher.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(vSwitcher.getDisplayedChild()==0)
					vSwitcher.setDisplayedChild(1);
				else
					vSwitcher.setDisplayedChild(0);
			}
			
		});
		button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if(vSwitcher.getDisplayedChild()==0)
					vSwitcher.setDisplayedChild(1);
				else
					vSwitcher.setDisplayedChild(0);
			}
			
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tag_preview, menu);
		return true;
	}
}
