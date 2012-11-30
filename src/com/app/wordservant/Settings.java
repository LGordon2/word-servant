package com.app.wordservant;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;

public class Settings extends PreferenceActivity implements OnSharedPreferenceChangeListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.settings);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

	@Override
	public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
		// TODO Auto-generated method stub
		if(key.equals("pref_key_notifications_enabled")){
	        //Set up alarm
	        AlarmManager aManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
	        Intent notification = new Intent(this, NotificationService.class);
	        PendingIntent p = PendingIntent.getService(this, 0, notification, PendingIntent.FLAG_UPDATE_CURRENT);
	        Calendar currentCalendar = Calendar.getInstance();
	        currentCalendar.add(Calendar.DAY_OF_MONTH, 1);
	        currentCalendar.set(Calendar.HOUR_OF_DAY,0);
	        currentCalendar.set(Calendar.MINUTE,0);
	        currentCalendar.set(Calendar.SECOND,0);
	        if(preferences.getBoolean(key, true)){
	        	aManager.setRepeating(AlarmManager.RTC, currentCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, p);
	        }else{
	        	aManager.cancel(p);
	        }
		}
	}
}
