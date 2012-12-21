package com.app.wordservant;

import java.util.Random;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class FlashcardQuizReviewFragment extends Fragment{

	private TextView mEditScriptureReference;
	private TextView mEditCategory;
	private TextView mEditScripture;
	private SparseIntArray mAllScriptureIds;
	private Cursor quizData;

	public void onViewCreated(View view, Bundle savedInstanceState){
		super.onViewCreated(view, savedInstanceState);
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
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
		RelativeLayout referenceLayout = (RelativeLayout) getView().findViewById(R.id.referenceLayout);
		TextView scriptureText = (TextView) getView().findViewById(R.id.scriptureText);
		referenceLayout.setOnClickListener(flipViewListener);
		scriptureText.setOnClickListener(flipViewListener);
		((Button) getView().findViewById(R.id.flipCardButton)).setOnClickListener(flipViewListener);

		mEditScriptureReference = (TextView) getView().findViewById(R.id.referenceText);
		mEditCategory = (TextView) getView().findViewById(R.id.scriptureTags);
		mEditScripture = (TextView) getView().findViewById(R.id.scriptureText);
		
		//Set up coloring.
		//Define the front and back of the cards.
		RelativeLayout frontCard;
		RelativeLayout backCard;
		if(sharedPreferences.getString("pref_key_review_select", "none").equals("showing_scripture")){
			cardFlipper.setDisplayedChild(1);
			frontCard = (RelativeLayout) cardFlipper.findViewById(R.id.scriptureLayout);
			backCard = (RelativeLayout) cardFlipper.findViewById(R.id.referenceLayout);
		}else{
			cardFlipper.setDisplayedChild(0);
			frontCard = (RelativeLayout) cardFlipper.findViewById(R.id.referenceLayout);
			backCard = (RelativeLayout) cardFlipper.findViewById(R.id.scriptureLayout);
		}

		//Change the color for these cards accordingly.
		frontCard.setBackgroundResource(R.drawable.front_of_flashcard);
		for (int i=0;i<frontCard.getChildCount();i++){
			TextView textView = (TextView) frontCard.getChildAt(i);
			textView.setTextColor(getResources().getColor(R.color.card_front_text_color));
		}
		backCard.setBackgroundResource(R.drawable.back_of_flashcard);
		for (int i=0;i<backCard.getChildCount();i++){
			TextView textView = (TextView) backCard.getChildAt(i);
			textView.setTextColor(getResources().getColor(R.color.card_back_text_color));
		}
	}
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}

	public void onStart(){
		super.onStart();
		quizData = new CursorLoader(getActivity()){
			public Cursor loadInBackground(){
				//Open the database.

				String [] columns = {
						WordServantContract.ScriptureEntry._ID,
						WordServantContract.ScriptureEntry.COLUMN_NAME_REFERENCE,
						WordServantContract.ScriptureEntry.COLUMN_NAME_TEXT,
						WordServantContract.ScriptureEntry.COLUMN_NAME_CORRECTLY_REVIEWED_COUNT,
						WordServantContract.ScriptureEntry.COLUMN_NAME_INCORRECTLY_REVIEWED_COUNT,
						WordServantContract.ScriptureEntry.COLUMN_NAME_SKIPPED_REVIEW_COUNT
				};
				String selection = WordServantContract.ScriptureEntry.COLUMN_NAME_NEXT_REVIEW_DATE+"<=date('now') OR "+
						WordServantContract.ScriptureEntry.COLUMN_NAME_TIMES_REVIEWED+">0";
				SQLiteDatabase db = new WordServantDbHelper(getActivity(), WordServantContract.DB_NAME, null, WordServantDbHelper.DATABASE_VERSION).getReadableDatabase();
				return db.query(
						WordServantContract.ScriptureEntry.TABLE_NAME, 
						columns, selection, null, null, null, null);
			}
		}.loadInBackground();

		//Put all Ids in a sparse int array.
		mAllScriptureIds  = new SparseIntArray(quizData.getCount());
		for(int i=0;i<quizData.getCount();i++){
			quizData.moveToPosition(i);
			mAllScriptureIds.append(i,quizData.getInt(0));
		}

		displayScriptureContent(quizData);

		Button nextButton = (Button) getActivity().findViewById(R.id.nextButton);
		nextButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				//Id.
				Integer id = quizData.getInt(0);
				mAllScriptureIds.put(quizData.getPosition(), id);

				//Update skipped.
				ContentValues contentValues = new ContentValues();
				contentValues.put(
						WordServantContract.ScriptureEntry.COLUMN_NAME_SKIPPED_REVIEW_COUNT,
						quizData.getInt(quizData.getColumnIndex(WordServantContract.ScriptureEntry.COLUMN_NAME_SKIPPED_REVIEW_COUNT))+1);
				SQLiteDatabase db = new WordServantDbHelper(
						getActivity(), 
						WordServantContract.DB_NAME, 
						null, 
						WordServantDbHelper.DATABASE_VERSION).getWritableDatabase();
				String whereClause = WordServantContract.ScriptureEntry._ID+"="+id;
				db.update(
						WordServantContract.ScriptureEntry.TABLE_NAME,
						contentValues, 
						whereClause, 
						null);

				//Display new scripture.
				displayScriptureContent(quizData);
			}

		});

		Button reviewedButton = (Button) getActivity().findViewById(R.id.finishedButton);
		reviewedButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				//Id.
				Integer id = quizData.getInt(0);

				//Update reviewed.
				ContentValues contentValues = new ContentValues();
				contentValues.put(
						WordServantContract.ScriptureEntry.COLUMN_NAME_CORRECTLY_REVIEWED_COUNT,
						quizData.getInt(quizData.getColumnIndex(WordServantContract.ScriptureEntry.COLUMN_NAME_CORRECTLY_REVIEWED_COUNT))+1);
				SQLiteDatabase db = new WordServantDbHelper(
						getActivity(), 
						WordServantContract.DB_NAME, 
						null, 
						WordServantDbHelper.DATABASE_VERSION).getWritableDatabase();
				String whereClause = WordServantContract.ScriptureEntry._ID+"="+id;
				db.update(
						WordServantContract.ScriptureEntry.TABLE_NAME,
						contentValues, 
						whereClause, 
						null);

				//Display new scripture.
				displayScriptureContent(quizData);

			}

		});

		Button missedButton = (Button) getActivity().findViewById(R.id.missedButton);
		missedButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				//Id.
				Integer id = quizData.getInt(0);

				//Update missed.
				ContentValues contentValues = new ContentValues();
				contentValues.put(
						WordServantContract.ScriptureEntry.COLUMN_NAME_INCORRECTLY_REVIEWED_COUNT,
						quizData.getInt(quizData.getColumnIndex(WordServantContract.ScriptureEntry.COLUMN_NAME_INCORRECTLY_REVIEWED_COUNT))+1);
				SQLiteDatabase db = new WordServantDbHelper(
						getActivity(), 
						WordServantContract.DB_NAME, 
						null, 
						WordServantDbHelper.DATABASE_VERSION).getWritableDatabase();
				String whereClause = WordServantContract.ScriptureEntry._ID+"="+id;
				db.update(
						WordServantContract.ScriptureEntry.TABLE_NAME,
						contentValues, 
						whereClause, 
						null);

				//Display new scripture.
				displayScriptureContent(quizData);
			}

		});
	}


	private Integer getRandomIdIndex(SparseIntArray allScriptureIds) {
		// TODO Auto-generated method stub
		if(allScriptureIds.size()==1)
			return 0;
		Integer randomIndex = ((Integer) Math.abs(new Random().nextInt()) % allScriptureIds.size());
		return mAllScriptureIds.keyAt(randomIndex) != quizData.getPosition() ? randomIndex : getRandomIdIndex(allScriptureIds);
	}

	protected void displayScriptureContent(Cursor scriptureQuery) {
		// TODO Auto-generated method stub
		try{
			if(mAllScriptureIds.size()==0){
				getActivity().finish();
				return;
			}
			if(mAllScriptureIds.size()==1){
				Button nextButton = (Button) getActivity().findViewById(R.id.nextButton);
				nextButton.setVisibility(Button.GONE);
			}

			//Get random index.
			Integer randomIndex = getRandomIdIndex(mAllScriptureIds);
			scriptureQuery.moveToPosition(mAllScriptureIds.keyAt(randomIndex));
			mAllScriptureIds.removeAt(randomIndex);
			mEditScriptureReference.setText(scriptureQuery.getString(1));

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
			mEditScripture.setText(scriptureQuery.getString(2));

		} catch(SQLiteException e){
			System.err.println("Database issue..");
			e.printStackTrace();
		}
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.flashcard_layout, null);
	}
}