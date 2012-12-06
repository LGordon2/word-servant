package com.app.wordservant;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
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
        displayLandingScreen();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        //Set up alarm
        AlarmManager aManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent notification = new Intent(this, NotificationService.class);
        PendingIntent p = PendingIntent.getService(this, 0, notification, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.add(Calendar.DAY_OF_MONTH, 1);
        currentCalendar.set(Calendar.HOUR_OF_DAY,0);
        currentCalendar.set(Calendar.MINUTE,0);
        currentCalendar.set(Calendar.SECOND,0);
        if(sharedPref.getBoolean("pref_key_notifications_enabled", true)){
        	aManager.setRepeating(AlarmManager.RTC, currentCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, p);
        }else{
        	aManager.cancel(p);
        }
    }

	/**
	 * Sets up the landing screen buttons.
	 */
	private void displayLandingScreen() {
		setContentView(R.layout.landing_screen);
		
		
		//Today's Memory Verses button
		Button todaysMemoryVerses = (Button) this.findViewById(R.id.todaysMemoryVerses);
		todaysMemoryVerses.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// Starts a new Today's Memory Verses activity.
				Intent intent = new Intent(v.getContext(),TodaysMemoryVerses.class);
				Toast.makeText(LandingScreen.this, R.string.title_activity_todays_memory_verses, Toast.LENGTH_SHORT).show();
		    	startActivity(intent);
			}
		});
		
		//Scripture Bank button
		Button scriptureBank = (Button) this.findViewById(R.id.scriptureBank);
		scriptureBank.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// Starts a new Scripture Bank activity.
				Intent intent = new Intent(v.getContext(),ScriptureBank.class);
				Toast.makeText(LandingScreen.this, R.string.title_activity_scripture_bank, Toast.LENGTH_SHORT).show();
		    	startActivity(intent);
			}
		});
		
		//Tag Preview button
		/*Button tagPreview = (Button) this.findViewById(R.id.tagPreview);
		tagPreview.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// Starts a new Scripture Bank activity.
				//Intent intent = new Intent(v.getContext(),SelectScripture.class);
				//Toast.makeText(LandingScreen.this, "Not yet implemented", Toast.LENGTH_SHORT).show();
		    	//startActivity(intent);
			}
		});*/

	}
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.landing_screen, menu);
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
