package com.app.wordservant.ui;

import java.util.Calendar;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import com.app.wordservant.R;
import com.app.wordservant.notifications.NotificationService;
import com.app.wordservant.util.BibleImporter;

public class LandingScreen extends Activity{


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		displayLandingScreen();
		if(!Bible.getInstance().isImported())
			setUpBible();
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

	private void setUpBible() {
		// TODO Auto-generated method stub
		Runnable bibleSetup = new BibleImporter(getResources().openRawResource(R.raw.kjv));
		ThreadPoolExecutor tpe = new ThreadPoolExecutor(10, 10, 10000, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>());
		tpe.execute(bibleSetup);
	}
	/**
	 * Sets up the landing screen buttons.
	 */
	private void displayLandingScreen() {
		setContentView(R.layout.landing_screen);

		ImageView image = (ImageView) findViewById(R.id.imageView1);
		Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
		if(display.getRotation() == Surface.ROTATION_90 || display.getRotation() == Surface.ROTATION_270){
			image.setScaleType(ScaleType.FIT_XY);
		}
		
		//Today's Memory Verses button
		((Button) this.findViewById(R.id.todaysMemoryVerses)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// Starts a new Today's Memory Verses activity.
				Intent intent = new Intent(LandingScreen.this,DueTodayNoScriptures.class);
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
				Intent intent = new Intent(v.getContext(),ScriptureBankActivity.class);
				Toast.makeText(LandingScreen.this, R.string.title_activity_scripture_bank, Toast.LENGTH_SHORT).show();
				startActivity(intent);
			}
		});

		//Quiz/Review button.
		((Button) this.findViewById(R.id.quizReview)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(v.getContext(),QuizReviewActivity.class);
				Toast.makeText(LandingScreen.this, R.string.title_activity_quiz_review, Toast.LENGTH_SHORT).show();
				startActivity(intent);
			}

		});


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
