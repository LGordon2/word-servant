package com.app.wordservant;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class FlashcardQuizReviewFragment extends Fragment {

	private TextView editScriptureReference;
	private TextView editCategory;
	private TextView editScripture;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}
	
	public void onStart(){
		super.onStart();
		Cursor quizData = new CursorLoader(getActivity()){
			public Cursor loadInBackground(){
				//Open the database.
				String [] columns = {
						WordServantContract.ScriptureEntry._ID,
						WordServantContract.ScriptureEntry.COLUMN_NAME_REFERENCE,
						WordServantContract.ScriptureEntry.COLUMN_NAME_TEXT
				};
				String selection = WordServantContract.ScriptureEntry.COLUMN_NAME_NEXT_REVIEW_DATE+"<=date('now') OR "+
						WordServantContract.ScriptureEntry.COLUMN_NAME_TIMES_REVIEWED+">0";
				SQLiteDatabase db = new WordServantDbHelper(getActivity(), WordServantContract.DB_NAME, null, WordServantDbHelper.DATABASE_VERSION).getReadableDatabase();
				return db.query(
						WordServantContract.ScriptureEntry.TABLE_NAME, 
						columns, selection, null, null, null, null);
			}
		}.loadInBackground();

		final ViewSwitcher cardFlipper = (ViewSwitcher) getView().findViewById(R.id.cardSwitcher);
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
		cardFlipper.setOnClickListener(flipViewListener);
		//Display the scripture information on the screen.
		editScriptureReference = (TextView) getView().findViewById(R.id.referenceText);
		editCategory = (TextView) getView().findViewById(R.id.scriptureTags);
		editScripture = (TextView) getView().findViewById(R.id.scriptureText);

		displayScriptureContent(quizData, 0);
	}

	protected void displayScriptureContent(Cursor scriptureQuery, int scriptureId) {
		// TODO Auto-generated method stub
		try{			
			SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
			if(sharedPref.getString("pref_key_review_select", "none").equals("showing_reference") ||
					sharedPref.getString("pref_key_review_select", "none").equals("showing_scripture")){
				ViewSwitcher cardFlipper = (ViewSwitcher) getActivity().findViewById(R.id.cardSwitcher);
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

			scriptureQuery.moveToFirst();
			editScriptureReference.setText(scriptureQuery.getString(1));

			//Set the tag text.
			/*String tagText = "";
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
			editCategory.setText(tagText);*/
			editScripture.setText(scriptureQuery.getString(2));
			if(scriptureQuery.getCount()==1){
				Button nextButton = (Button) getActivity().findViewById(R.id.nextButton);
				nextButton.setVisibility(Button.GONE);
			}
		} catch(SQLiteException e){
			System.err.println("Database issue..");
			e.printStackTrace();
		}
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.flashcard_layout, null);
	}

}
