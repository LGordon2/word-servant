package com.app.wordservant;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class BootBroadcastReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
        //Set up alarm
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        AlarmManager aManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent notification = new Intent(context, NotificationService.class);
        PendingIntent p = PendingIntent.getService(context, 0, notification, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.add(Calendar.DAY_OF_MONTH, 1);
        currentCalendar.set(Calendar.HOUR_OF_DAY,0);
        currentCalendar.set(Calendar.MINUTE,0);
        currentCalendar.set(Calendar.SECOND,0);
        if(sharedPref.getBoolean("pref_key_notifications_enabled", true)){
        	aManager.setRepeating(AlarmManager.RTC, currentCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, p);
        }else{
        	aManager.cancel(p);
        }}

}
