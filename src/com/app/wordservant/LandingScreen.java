package com.app.wordservant;

import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
		Runnable bibleSetup =new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				//Bible bible = Bible.getInstance();
				Bible.BibleBook b = null;
				Bible.BibleChapter c = null;
				Bible.BibleVerse v = null;
				Integer currentBook = -1;
				try {
					XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
					factory.setNamespaceAware(true);
					XmlPullParser xpp = factory.newPullParser();
					xpp.setInput(getResources().openRawResource(R.raw.kjv), null);

					int eventType = xpp.getEventType();
					while(eventType != XmlPullParser.END_DOCUMENT){
						if(eventType == XmlPullParser.START_TAG){
							if(xpp.getName().equals("BIBLEBOOK")){
								currentBook += 1;
								b = Bible.getInstance().new BibleBook(currentBook, xpp.getAttributeValue(null, "bname"));
							}else if(xpp.getName().equals("CHAPTER")){
								c = Bible.getInstance().new BibleChapter(xpp.getAttributeValue(null, "cnumber"));
							}else if(xpp.getName().equals("VERS")){
								v = Bible.getInstance().new BibleVerse(xpp.getAttributeValue(null, "vnumber"),xpp.nextText());
								c.addVerse(v);
							}

						}else if(eventType == XmlPullParser.END_TAG){
							if(xpp.getName().equals("CHAPTER") && currentBook != null){
								b.addChapter(c);
							}else if(xpp.getName().equals("BIBLEBOOK")){
								Bible.getInstance().addBook(b);
							}
						}
						eventType = xpp.next();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		};
		ThreadPoolExecutor tpe = new ThreadPoolExecutor(10, 10, 10000, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>());
		tpe.execute(bibleSetup);
		tpe.shutdown();
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
		Button tagPreview = (Button) this.findViewById(R.id.tagPreview);
		tagPreview.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// Starts a new Scripture Bank activity.
				Intent intent = new Intent(v.getContext(),ScriptureBank.class);
				//Toast.makeText(LandingScreen.this, "Not yet implemented", Toast.LENGTH_SHORT).show();
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
