package com.app.wordservant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class ScriptureReview extends Activity {

	private TextView editScriptureReference;
	private TextView editCategory;
	private TextView editScripture;
	private SQLiteDatabase wordservant_db;
	private Cursor unreviewedScriptureQuery;
	private int positionOnScreen;
	private Cursor scriptureQuery;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scripture_review);

		//Display the scripture information on the screen.
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		RelativeLayout reviewLayout = (RelativeLayout) findViewById(R.id.scriptureReviewLayout);
		RelativeLayout.LayoutParams scriptureReviewLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		scriptureReviewLayoutParams.addRule(RelativeLayout.ABOVE,R.id.dueTodayButtonLayout);
		
		//Display flashcards if the setting is enabled.
		if(sharedPref.getString("pref_key_review_select", "none").equals("showing_reference") ||
				sharedPref.getString("pref_key_review_select", "none").equals("showing_scripture")){
			RelativeLayout flashCardLayout = (RelativeLayout) this.getLayoutInflater().inflate(R.layout.flashcard_layout, null);
				
			flashCardLayout.setLayoutParams(scriptureReviewLayoutParams);
			reviewLayout.addView(flashCardLayout);

			final ViewSwitcher cardFlipper = (ViewSwitcher) findViewById(R.id.cardSwitcher);
			OnClickListener flipViewListener = new OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(cardFlipper.getDisplayedChild()==0)
						cardFlipper.setDisplayedChild(1);
					else
						cardFlipper.setDisplayedChild(0);
				}
			};
			LinearLayout referenceLayout = (LinearLayout) findViewById(R.id.referenceLayout);
			TextView scriptureText = (TextView) findViewById(R.id.scriptureText);
			referenceLayout.setOnClickListener(flipViewListener);
			scriptureText.setOnClickListener(flipViewListener);
			((Button) findViewById(R.id.flipCardButton)).setOnClickListener(flipViewListener);

			editScriptureReference = (TextView) findViewById(R.id.referenceText);
			editCategory = (TextView) findViewById(R.id.scriptureTags);
			editScripture = (TextView) findViewById(R.id.scriptureText);
		}else{
			RelativeLayout noEditScriptureLayout = (RelativeLayout) this.getLayoutInflater().inflate(R.layout.layout_no_edit_scripture, null);
			noEditScriptureLayout.setLayoutParams(scriptureReviewLayoutParams);
			reviewLayout.addView(noEditScriptureLayout);
			editScriptureReference = (TextView) findViewById(R.id.dueTodayScriptureReference);
			editCategory = (TextView) findViewById(R.id.dueTodayTags);
			editScripture = (TextView) findViewById(R.id.scriptureText);
		}

		//Get the date for today.
		SimpleDateFormat dbDateFormat = new SimpleDateFormat(getResources().getString(R.string.date_format), Locale.US);
		dbDateFormat.format(Calendar.getInstance().getTime());

		//Open the database.
		wordservant_db = new WordServantDbHelper(this, WordServantContract.DB_NAME, null, WordServantDbHelper.DATABASE_VERSION).getReadableDatabase();
		positionOnScreen = this.getIntent().getIntExtra("positionOnScreen", 0);
		final Bundle bundledScriptureList = this.getIntent().getBundleExtra("bundledScriptureList");
		displayScriptureContent(bundledScriptureList.getInt(String.valueOf(positionOnScreen)));

		//Check all unreviewedScriptures.
		String [] columnsToRetrieve = {WordServantContract.ScriptureEntry._ID,
				WordServantContract.ScriptureEntry.COLUMN_NAME_NEXT_REVIEW_DATE};
		unreviewedScriptureQuery = wordservant_db.query(
				WordServantContract.ScriptureEntry.TABLE_NAME, 
				columnsToRetrieve, 
				WordServantContract.ScriptureEntry.COLUMN_NAME_NEXT_REVIEW_DATE+"=date('now') OR "+WordServantContract.ScriptureEntry._ID+"="+bundledScriptureList.getInt(String.valueOf(positionOnScreen)), 
				null, null, null, null);

		//Set the row in the unreviewed scripture query to match the selected item in Today's Memory Verses.
		unreviewedScriptureQuery.moveToFirst();
		while(unreviewedScriptureQuery.getInt(0)!=bundledScriptureList.getInt(String.valueOf(positionOnScreen))){
			unreviewedScriptureQuery.moveToNext();
			if(unreviewedScriptureQuery.isAfterLast()){
				unreviewedScriptureQuery.moveToFirst();
			}
		}

		Button nextButton = (Button) findViewById(R.id.dueTodayNextButton);
		nextButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				setNextScriptureId();
				displayScriptureContent(unreviewedScriptureQuery.getInt(0));
			}

		});

		Button reviewedButton = (Button) findViewById(R.id.dueTodayFinishedButton);
		reviewedButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				String [] columnsToRetrieve = {WordServantContract.ScriptureEntry._ID};
				updateReviewedScripture(getApplicationContext(), unreviewedScriptureQuery.getInt(0));
				unreviewedScriptureQuery = wordservant_db.query(
						WordServantContract.ScriptureEntry.TABLE_NAME, 
						columnsToRetrieve, 
						WordServantContract.ScriptureEntry.COLUMN_NAME_NEXT_REVIEW_DATE+"=date('now')",
						null, null, null, null);
				unreviewedScriptureQuery.moveToFirst();
				//Select the next.
				setNextScriptureId();
				if(unreviewedScriptureQuery.getCount()==0){
					finish();
					return;
				}
				displayScriptureContent(unreviewedScriptureQuery.getInt(0));
			}

		});
	}
	protected void displayScriptureContent(int scriptureId) {
		// TODO Auto-generated method stub
		try{			
			SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
			if(sharedPref.getString("pref_key_review_select", "none").equals("showing_reference") ||
					sharedPref.getString("pref_key_review_select", "none").equals("showing_scripture")){
				ViewSwitcher cardFlipper = (ViewSwitcher) findViewById(R.id.cardSwitcher);
				cardFlipper.reset();
				
				//Define the front and back of the cards.
				LinearLayout frontCard;
				LinearLayout backCard;
				if(sharedPref.getString("pref_key_review_select", "none").equals("showing_scripture")){
					cardFlipper.setDisplayedChild(1);
					frontCard = (LinearLayout) cardFlipper.findViewById(R.id.scriptureLayout);
					backCard = (LinearLayout) cardFlipper.findViewById(R.id.referenceLayout);
				}else{
					cardFlipper.setDisplayedChild(0);
					frontCard = (LinearLayout) cardFlipper.findViewById(R.id.referenceLayout);
					backCard = (LinearLayout) cardFlipper.findViewById(R.id.scriptureLayout);
				}
				
				//Change the color for these cards accordingly.
				frontCard.setBackgroundColor(getResources().getColor(android.R.color.black));
				for (int i=0;i<frontCard.getChildCount();i++){
					TextView textView = (TextView) frontCard.getChildAt(i);
					textView.setBackgroundColor(getResources().getColor(android.R.color.black));
					textView.setTextColor(getResources().getColor(android.R.color.white));
				}
				backCard.setBackgroundColor(getResources().getColor(android.R.color.white));
				for (int i=0;i<backCard.getChildCount();i++){
					TextView textView = (TextView) backCard.getChildAt(i);
					textView.setBackgroundColor(getResources().getColor(android.R.color.white));
					textView.setTextColor(getResources().getColor(android.R.color.black));
				}
			}
			String [] columns_to_retrieve = {
					WordServantContract.ScriptureEntry.COLUMN_NAME_REFERENCE,
					WordServantContract.ScriptureEntry.COLUMN_NAME_TEXT,
					WordServantContract.ScriptureEntry._ID};
			scriptureQuery = wordservant_db.query(
					WordServantContract.ScriptureEntry.TABLE_NAME, 
					columns_to_retrieve, 
					WordServantContract.ScriptureEntry._ID+"="+scriptureId, 
					null, null, null, null);
			scriptureQuery.moveToFirst();
			editScriptureReference.setText(scriptureQuery.getString(0));
			
			//Set the tag text.
			String tagText = "";
			Cursor tagTextQuery = wordservant_db.rawQuery("select "+WordServantContract.TagEntry.COLUMN_NAME_TAG_NAME+
					" from "+WordServantContract.CategoryEntry.TABLE_NAME+" c join "+
					WordServantContract.ScriptureEntry.TABLE_NAME+" s on "+
					"(c."+WordServantContract.CategoryEntry.COLUMN_NAME_SCRIPTURE_ID+"=s."+
					WordServantContract.ScriptureEntry._ID+") join "+
					WordServantContract.TagEntry.TABLE_NAME+" t on (c."+WordServantContract.CategoryEntry.COLUMN_NAME_TAG_ID+
					"=t."+WordServantContract.TagEntry._ID+") where "+
					"s."+WordServantContract.ScriptureEntry._ID+"="+scriptureQuery.getInt(2),null);
			for(int i=0;i<tagTextQuery.getCount();i++){
				tagTextQuery.moveToPosition(i);
				tagText += tagTextQuery.getString(0);
				if(i<tagTextQuery.getCount()-1)
					tagText += ", ";
			}
			editCategory.setText(tagText);
			editScripture.setText(scriptureQuery.getString(1));
			String [] columnsToRetrieve = {WordServantContract.ScriptureEntry._ID};
			unreviewedScriptureQuery = wordservant_db.query(
					WordServantContract.ScriptureEntry.TABLE_NAME, 
					columnsToRetrieve, 
					WordServantContract.ScriptureEntry.COLUMN_NAME_NEXT_REVIEW_DATE+"=date('now')",
					null, null, null, null);
			if(unreviewedScriptureQuery.getCount()==1){
				Button nextButton = (Button) findViewById(R.id.dueTodayNextButton);
				nextButton.setVisibility(Button.GONE);
			}
		} catch(SQLiteException e){
			System.err.println("Database issue..");
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_due_today, menu);
		return true;
	}
	private void setNextScriptureId(){
		//If there are no or just one we just need to display a message and return that scripture id.
		if(unreviewedScriptureQuery.getCount()==0){
			return;
		}
		else if(unreviewedScriptureQuery.getCount()==1 && unreviewedScriptureQuery.getInt(0)==scriptureQuery.getInt(3)){
			Toast.makeText(getApplicationContext(), "This is the last unchecked scripture.", Toast.LENGTH_SHORT).show();
		}
		else{
			unreviewedScriptureQuery.moveToNext();
			if(unreviewedScriptureQuery.isAfterLast()){
				unreviewedScriptureQuery.moveToFirst();
			}
		}
	}
	public static void updateReviewedScripture(Context context, int scriptureId){
		SQLiteDatabase wordservant_db = new WordServantDbHelper(context, WordServantContract.DB_NAME, null, WordServantDbHelper.DATABASE_VERSION).getWritableDatabase();
		String [] columns_to_retrieve = {
				WordServantContract.ScriptureEntry.COLUMN_NAME_NEXT_REVIEW_DATE, 
				WordServantContract.ScriptureEntry.COLUMN_NAME_SCHEDULE,
				WordServantContract.ScriptureEntry.COLUMN_NAME_TIMES_REVIEWED,
				WordServantContract.ScriptureEntry.COLUMN_NAME_LAST_REVIEWED_DATE};
		Cursor scriptureQuery = wordservant_db.query(
				WordServantContract.ScriptureEntry.TABLE_NAME, 
				columns_to_retrieve, 
				WordServantContract.ScriptureEntry._ID+"="+scriptureId, 
				null, null, null, null);
		scriptureQuery.moveToFirst();

		// Update the database showing that the scripture was reviewed.
		ContentValues updatedValues = new ContentValues();
		SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String todaysDate = dbDateFormat.format(Calendar.getInstance().getTime());

		//Update the times reviewed.
		Date currentDate = Calendar.getInstance().getTime();
		currentDate.setHours(0);
		currentDate.setMinutes(0);
		currentDate.setSeconds(0);

		try {
			if(scriptureQuery.getString(3) == null){
				updatedValues.put(WordServantContract.ScriptureEntry.COLUMN_NAME_TIMES_REVIEWED, 1);
			}

			else if(dbDateFormat.parse(scriptureQuery.getString(3)).before(currentDate) && !dbDateFormat.parse(scriptureQuery.getString(3)).equals(currentDate)){
				updatedValues.put(WordServantContract.ScriptureEntry.COLUMN_NAME_TIMES_REVIEWED, scriptureQuery.getInt(2)+1);
				if(scriptureQuery.getInt(2) == 6){
					updatedValues.put(WordServantContract.ScriptureEntry.COLUMN_NAME_SCHEDULE, "weekly");
				}else if(scriptureQuery.getInt(2)==13){
					updatedValues.put(WordServantContract.ScriptureEntry.COLUMN_NAME_SCHEDULE, "monthly");
				}else if(scriptureQuery.getInt(2)==20){
					updatedValues.put(WordServantContract.ScriptureEntry.COLUMN_NAME_SCHEDULE, "yearly");
				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Update the last reviewed date to be today.
		updatedValues.put(WordServantContract.ScriptureEntry.COLUMN_NAME_LAST_REVIEWED_DATE, todaysDate);

		//Calculate when the next review should be and update that date.
		Calendar calculatedDate = Calendar.getInstance();
		if(scriptureQuery.getString(1).equals("daily")){
			calculatedDate.add(Calendar.DATE, 1);
		}else if(scriptureQuery.getString(1).equals("weekly")){
			calculatedDate.add(Calendar.DATE, 7);
		}else if(scriptureQuery.getString(1).equals("monthly")){
			calculatedDate.add(Calendar.MONTH, 1);
		}else if(scriptureQuery.getString(1).equals("yearly")){
			calculatedDate.add(Calendar.YEAR, 1);
		}

		updatedValues.put(WordServantContract.ScriptureEntry.COLUMN_NAME_NEXT_REVIEW_DATE, dbDateFormat.format(calculatedDate.getTime()));

		wordservant_db.update(
				WordServantContract.ScriptureEntry.TABLE_NAME, 
				updatedValues, 
				WordServantContract.ScriptureEntry._ID+"="+scriptureId, 
				null);

	}
	public void onDestroy(){
		super.onDestroy();
		wordservant_db.close();
	}
}
