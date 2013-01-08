package com.app.wordservant.ui;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.app.wordservant.R;
import com.app.wordservant.R.color;
import com.app.wordservant.R.drawable;
import com.app.wordservant.R.id;
import com.app.wordservant.R.layout;
import com.app.wordservant.R.string;
import com.app.wordservant.helper_classes.ImageAdapter;
import com.app.wordservant.provider.WordServantContract;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class ScriptureReview extends FragmentActivity {

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scripture_review);
	}

	public static class ScriptureReviewFragment extends Fragment {
		private TextView mEditScriptureReference;
		private TextView mEditCategory;
		private TextView mEditScripture;
		private Integer mFirstSelectedScriptureId;
		private Cursor mUnreviewedScriptureQuery;
		private Uri fileUri;
		private ImageAdapter adapter;
		public static final int MEDIA_TYPE_IMAGE = 1;
		public static final int MEDIA_TYPE_VIDEO = 2;
		private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
		private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;


		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
			if(sharedPreferences.getString("pref_key_review_select", "none").equals("showing_reference") ||
					sharedPreferences.getString("pref_key_review_select", "none").equals("showing_scripture")){
				return inflater.inflate(R.layout.flashcard_layout, null);
			}
			return inflater.inflate(R.layout.layout_no_edit_scripture, null);
		}


		/** Create a file Uri for saving an image or video */
		private static Uri getOutputMediaFileUri(int type){
			return Uri.fromFile(getOutputMediaFile(type));
		}

		/** Create a File for saving an image or video */
		private static File getOutputMediaFile(int type){
			// To be safe, you should check that the SDCard is mounted
			// using Environment.getExternalStorageState() before doing this.

			File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
					Environment.DIRECTORY_PICTURES), "WordServant");
			// This location works best if you want the created images to be shared
			// between applications and persist after your app has been uninstalled.

			// Create the storage directory if it does not exist
			if (! mediaStorageDir.exists()){
				if (! mediaStorageDir.mkdirs()){
					Log.d("WordServant", "failed to create directory");
					return null;
				}
			}

			// Create a media file name
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			File mediaFile;
			if (type == MEDIA_TYPE_IMAGE){
				mediaFile = new File(mediaStorageDir.getPath() + File.separator +
						"IMG_"+ timeStamp + ".jpg");
			} else if(type == MEDIA_TYPE_VIDEO) {
				mediaFile = new File(mediaStorageDir.getPath() + File.separator +
						"VID_"+ timeStamp + ".mp4");
			} else {
				return null;
			}

			return mediaFile;
		}

		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
				if (resultCode == RESULT_OK) {
					adapter.notifyDataSetChanged();
					// Image captured and saved to fileUri specified in the Intent
					// Toast.makeText(getActivity(), "Image saved to:\n" +
					//        data.getData(), Toast.LENGTH_LONG).show();
				} else if (resultCode == RESULT_CANCELED) {
					// User cancelled the image capture
				} else {
					// Image capture failed, advise user
				}
			}

			if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
				if (resultCode == RESULT_OK) {
					// Video captured and saved to fileUri specified in the Intent
					Toast.makeText(getActivity(), "Video saved to:\n" +
							data.getData(), Toast.LENGTH_LONG).show();
				} else if (resultCode == RESULT_CANCELED) {
					// User cancelled the video capture
				} else {
					// Video capture failed, advise user
				}
			}
		}

		public void onViewCreated(View view, Bundle savedInstanceState){
			super.onViewCreated(view, savedInstanceState);

			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
			//Display flashcards if the setting is enabled.
			if(sharedPreferences.getString("pref_key_review_select", "none").equals("showing_reference") ||
					sharedPreferences.getString("pref_key_review_select", "none").equals("showing_scripture")){

				//Set up click events, as well as fields.
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
			}else{
				mEditScriptureReference = (TextView) getView().findViewById(R.id.dueTodayScriptureReference);
				mEditCategory = (TextView) getView().findViewById(R.id.dueTodayTags);
				mEditScripture = (TextView) getView().findViewById(R.id.scriptureText);



				/*@SuppressWarnings("deprecation")
				Gallery gallery = (Gallery) getView().findViewById(R.id.gallery1);
				adapter = new ImageAdapter(getActivity());
				gallery.setAdapter(adapter);

				//Find all the WordServantFiles and add them to the gallery.
				File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
						Environment.DIRECTORY_PICTURES), "WordServant");
				if(mediaStorageDir.exists()){
					for(int i=0;i<mediaStorageDir.listFiles().length;i++){
						adapter.addImage(mediaStorageDir.listFiles()[i]);
					}
				}

				gallery.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
						File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
								Environment.DIRECTORY_PICTURES), "WordServant");
						((ImageView) getView().findViewById(R.id.mainImage)).setImageBitmap(
								ImageAdapter.decodeSampledBitmapFromResource(mediaStorageDir.listFiles()[position], 140, 140));
					}
				});

				((ImageButton) getView().findViewById(R.id.takePictureButton)).setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View view) {
						// create Intent to take a picture and return control to the calling application
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

						fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
						intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

						// start the image capture Intent
						startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
						//((ImageView) getView().findViewById(R.id.mainImage)).setImageURI(fileUri);
					}

				});*/
			}
		}

		public void onStart(){
			super.onStart();

			//Get the date for today.
			SimpleDateFormat dbDateFormat = new SimpleDateFormat(getResources().getString(R.string.date_format), Locale.US);
			dbDateFormat.format(Calendar.getInstance().getTime());

			//Open the database.
			Integer positionOnScreen = getActivity().getIntent().getIntExtra("positionOnScreen", 0);
			final Bundle bundledScriptureList = getActivity().getIntent().getBundleExtra("bundledScriptureList");
			mFirstSelectedScriptureId = bundledScriptureList.getInt(String.valueOf(positionOnScreen));

			displayScriptureContent(mFirstSelectedScriptureId);

			//Check all unreviewedScriptures.
			String [] columnsToRetrieve = {WordServantContract.ScriptureEntry._ID,
					WordServantContract.ScriptureEntry.COLUMN_NAME_NEXT_REVIEW_DATE};
			mUnreviewedScriptureQuery = getActivity().getContentResolver().query(
					WordServantContract.ScriptureEntry.CONTENT_URI, 
					columnsToRetrieve, 
					WordServantContract.ScriptureEntry.COLUMN_NAME_NEXT_REVIEW_DATE+"=date('now','localtime') OR "+
							WordServantContract.ScriptureEntry._ID+"="+mFirstSelectedScriptureId, 
							null, null);

			//Set the row in the unreviewed scripture query to match the selected item in Today's Memory Verses.
			mUnreviewedScriptureQuery.moveToFirst();
			while(mUnreviewedScriptureQuery.getInt(0)!=bundledScriptureList.getInt(String.valueOf(positionOnScreen))){
				mUnreviewedScriptureQuery.moveToNext();
				if(mUnreviewedScriptureQuery.isAfterLast()){
					mUnreviewedScriptureQuery.moveToFirst();
				}
			}

			Button nextButton = (Button) getActivity().findViewById(R.id.dueTodayNextButton);
			if(mUnreviewedScriptureQuery.getCount()==1){
				nextButton.setVisibility(Button.GONE);
			}else{
				nextButton.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View view) {
						setNextScriptureId();
						displayScriptureContent(mUnreviewedScriptureQuery.getInt(0));
					}

				});
			}
			Button reviewedButton = (Button) getActivity().findViewById(R.id.dueTodayFinishedButton);
			reviewedButton.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View view) {
					//Update the database.
					Calendar calendar = Calendar.getInstance();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					String scriptureDateNextReviewDate = mUnreviewedScriptureQuery.getString(mUnreviewedScriptureQuery.getColumnIndex(WordServantContract.ScriptureEntry.COLUMN_NAME_NEXT_REVIEW_DATE));
					try {
						if(dateFormat.parse(scriptureDateNextReviewDate).compareTo(calendar.getTime())<=0){
							//if(mUnreviewedScriptureQuery.getString(mUnreviewedScriptureQuery.getColumnIndex(WordServantContract.ScriptureEntry.COLUMN_NAME_NEXT_REVIEW_DATE))<=(dateFormat.format(calendar.getTime()))){
							updateReviewedScripture(getActivity(), mUnreviewedScriptureQuery.getInt(0), true);
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if(mUnreviewedScriptureQuery.getInt(0) == mFirstSelectedScriptureId){
						mFirstSelectedScriptureId = -1;
					}

					if(mUnreviewedScriptureQuery.getCount()==1){
						getActivity().finish();
						return;
					}

					String [] columnsToRetrieve = {WordServantContract.ScriptureEntry._ID};
					mUnreviewedScriptureQuery = getActivity().getContentResolver().query(
							WordServantContract.ScriptureEntry.CONTENT_URI, 
							columnsToRetrieve, 
							WordServantContract.ScriptureEntry.COLUMN_NAME_NEXT_REVIEW_DATE+"=date('now','localtime') OR "+
									WordServantContract.ScriptureEntry._ID+"="+mFirstSelectedScriptureId,
									null, null);
					mUnreviewedScriptureQuery.moveToFirst();
					Button nextButton = (Button) getActivity().findViewById(R.id.dueTodayNextButton);
					if(mUnreviewedScriptureQuery.getCount()==1){
						nextButton.setVisibility(Button.GONE);
					}
					//Select the next.
					setNextScriptureId();
					displayScriptureContent(mUnreviewedScriptureQuery.getInt(0));


				}

			});
		}

		public void updateReviewedScripture(Context context, int scriptureId, boolean increment){
			String incrementOrDecrement = increment
					?"INCREMENT_TIMES_REVIEWED":"DECREMENT_TIMES_REVIEWED";
			getActivity().getContentResolver().update(
					Uri.withAppendedPath(WordServantContract.ScriptureEntry.CONTENT_ID_URI_BASE, String.valueOf(scriptureId)+"/"+incrementOrDecrement), null, null, null);
		}

		protected void displayScriptureContent(int scriptureId) {
			// TODO Auto-generated method stub
			try{		
				String [] columns_to_retrieve = {
						WordServantContract.ScriptureEntry.COLUMN_NAME_REFERENCE,
						WordServantContract.ScriptureEntry.COLUMN_NAME_TEXT,
						WordServantContract.ScriptureEntry._ID,
						WordServantContract.ScriptureEntry.COLUMN_NAME_NEXT_REVIEW_DATE};
				Cursor scriptureQuery = getActivity().getContentResolver().query(
						Uri.withAppendedPath(WordServantContract.ScriptureEntry.CONTENT_ID_URI_BASE,String.valueOf(scriptureId)), 
						columns_to_retrieve, 
						null, null, null);
				scriptureQuery.moveToFirst();
				mEditScriptureReference.setText(scriptureQuery.getString(0));

				//Set the tag text.
				/*String tagText = "";
				Cursor tagTextQuery = mDatabaseConnection.rawQuery("select "+WordServantContract.TagEntry.COLUMN_NAME_TAG_NAME+
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
				mEditScripture.setText(Html.fromHtml(scriptureQuery.getString(1)));
			} catch(SQLiteException e){
				System.err.println("Database issue..");
				e.printStackTrace();
			}
		}

		private void setNextScriptureId(){
			//If there are no or just one we just need to display a message and return that scripture id.
			if(mUnreviewedScriptureQuery.getCount()==0){
				return;
			}else{
				mUnreviewedScriptureQuery.moveToNext();
				if(mUnreviewedScriptureQuery.isAfterLast()){
					mUnreviewedScriptureQuery.moveToFirst();
				}
			}
		}

	}
}