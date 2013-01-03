package com.app.wordservant;

import java.util.Random;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.text.Html;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class QuizReviewActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz_review);
		// Show the Up button in the action bar.
		//getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_quiz_review, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public static class FlashcardQuizReviewFragment extends Fragment{

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
			for (int i=0;i<frontCard.getChildCount()-1;i++){
				TextView textView = (TextView) frontCard.getChildAt(i);
				textView.setTextColor(getResources().getColor(R.color.card_front_text_color));
			}
			backCard.setBackgroundResource(R.drawable.back_of_flashcard);
			for (int i=0;i<backCard.getChildCount()-1;i++){
				TextView textView = (TextView) backCard.getChildAt(i);
				textView.setTextColor(getResources().getColor(R.color.word_servant_purple));
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
					String selection = WordServantContract.ScriptureEntry.COLUMN_NAME_NEXT_REVIEW_DATE+"<=date('now','localtime') OR "+
							WordServantContract.ScriptureEntry.COLUMN_NAME_TIMES_REVIEWED+">0";
					return getActivity().getContentResolver().query(
							WordServantContract.ScriptureEntry.CONTENT_URI, 
							columns, selection, null, null);
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
					getActivity().getContentResolver().update(
							Uri.withAppendedPath(WordServantContract.ScriptureEntry.CONTENT_ID_URI_BASE,String.valueOf(id)),
							contentValues, null, null);

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
					getActivity().getContentResolver().update(
							Uri.withAppendedPath(WordServantContract.ScriptureEntry.CONTENT_ID_URI_BASE,String.valueOf(id)),
							contentValues, null, null);
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
					getActivity().getContentResolver().update(
							Uri.withAppendedPath(WordServantContract.ScriptureEntry.CONTENT_ID_URI_BASE,String.valueOf(id)),
							contentValues, null, null);

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
				mEditScripture.setText(Html.fromHtml(scriptureQuery.getString(2)));

			} catch(SQLiteException e){
				System.err.println("Database issue..");
				e.printStackTrace();
			}
		}

		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
			return inflater.inflate(R.layout.flashcard_layout, null);
		}
	}
	
}
