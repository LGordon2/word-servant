package com.app.wordservant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class LandingScreen extends Activity{


	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onStart();
        displayLandingScreen();
    }
	private void displayLandingScreen() {
		// TODO Auto-generated method stub
		setContentView(R.layout.landing_screen);
		
		Button todaysMemoryVerses = (Button) this.findViewById(R.id.todaysMemoryVerses);
		todaysMemoryVerses.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(v.getContext(),TodaysMemoryVerses.class);
				Toast.makeText(LandingScreen.this, R.string.title_activity_todays_memory_verses, Toast.LENGTH_SHORT).show();
		    	startActivity(intent);
			}
		});
		
		Button scriptureBank = (Button) this.findViewById(R.id.scriptureBank);
		scriptureBank.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(v.getContext(),ScriptureBank.class);
				Toast.makeText(LandingScreen.this, R.string.title_activity_scripture_bank, Toast.LENGTH_SHORT).show();
		    	startActivity(intent);
			}
		});
	}
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.landing_screen, menu);
        return true;
    }


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.menu_settings:
	        	Intent intent = new Intent(this, Settings.class);
	        	startActivity(intent);
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}
