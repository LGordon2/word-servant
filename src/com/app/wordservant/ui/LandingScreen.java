package com.app.wordservant.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.LinkedBlockingQueue;
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
import com.app.wordservant.bible.Bible;
import com.app.wordservant.notifications.NotificationService;
import com.app.wordservant.util.Constants;
import com.app.wordservant.util.ImportBibleBook;

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

	private class ImportBible implements Runnable{

		@Override
		public void run(){
			// TODO Auto-generated method stub
			android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
			Bible.getInstance().mImportStatus = Constants.IMPORT_STATUS_IMPORTING;
			ThreadPoolExecutor tpe = new ThreadPoolExecutor(20, 20, 10000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.acts),"acts",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.amos),"amos",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.colossians),"colossians",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.daniel),"daniel",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.deuteronomy),"deuteronomy",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.ecclesiastes),"ecclesiastes",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.ephesians),"ephesians",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.esther),"esther",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.exodus),"exodus",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.ezekiel),"ezekiel",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.ezra),"ezra",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.galatians),"galatians",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.genesis),"genesis",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.habakkuk),"habakkuk",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.haggai),"haggai",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.hebrews),"hebrews",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.hosea),"hosea",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.isaiah),"isaiah",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.james),"james",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.jeremiah),"jeremiah",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.job),"job",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.joel),"joel",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.john),"john",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.jonah),"jonah",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.joshua),"joshua",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.jude),"jude",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.judges),"judges",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.lamentations),"lamentations",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.leviticus),"leviticus",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.luke),"luke",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.mark),"mark",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.matthew),"matthew",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.micah),"micah",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.nahum),"nahum",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.nehemiah),"nehemiah",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.numbers),"numbers",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.obadiah),"obadiah",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.philemon),"philemon",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.philippians),"philippians",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.proverbs),"proverbs",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.psalms),"psalms",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.revelation),"revelation",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.romans),"romans",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.ruth),"ruth",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.song_of_solomon),"song_of_solomon",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.titus),"titus",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.zechariah),"zechariah",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.zephaniah),"zephaniah",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.first_chronicles),"first_chronicles",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.first_corinthians),"first_corinthians",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.first_john),"first_john",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.first_kings),"first_kings",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.first_peter),"first_peter",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.first_samuel),"first_samuel",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.first_thessalonians),"first_thessalonians",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.first_timothy),"first_timothy",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.second_chronicles),"second_chronicles",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.second_corinthians),"second_corinthians",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.second_john),"second_john",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.second_kings),"second_kings",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.second_peter),"second_peter",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.second_samuel),"second_samuel",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.second_thessalonians),"second_thessalonians",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.second_timothy),"second_timothy",0));
			tpe.execute(new ImportBibleBook(getResources().openRawResource(R.raw.third_john),"third_john",0));
			
			while(tpe.getCompletedTaskCount()<tpe.getTaskCount()){}
			Bible.getInstance().importDone();
		}
		
	}
	
	private void setUpBible() {
		// TODO Auto-generated method stub
		new Thread(new ImportBible()).start();
		/*Runnable bibleSetup = new BibleImporter(getResources().openRawResource(R.raw.net));
		ThreadPoolExecutor tpe = new ThreadPoolExecutor(10, 10, 10000, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>());
		tpe.execute(bibleSetup);*/
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
				Intent intent = new Intent(LandingScreen.this,ScriptureReview.class);
				intent.putIntegerArrayListExtra("unreviewedScriptureIds", new ArrayList<Integer>());
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
