package com.app.wordservant;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.CursorLoader;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class AddTagDialogFragment extends DialogFragment {
	private class TagListLoader extends CursorLoader{
		public TagListLoader(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		public Cursor loadInBackground(){
			SQLiteDatabase db = new WordServantDbHelper(getActivity(), WordServantContract.DATABASE_NAME, null, WordServantDbHelper.DATABASE_VERSION).getWritableDatabase();
			String [] columns = {
					WordServantContract.TagEntry._ID,
					WordServantContract.TagEntry.COLUMN_NAME_TAG_NAME
			};
			return db.query(WordServantContract.TagEntry.TABLE_NAME, columns, null, null, null, null, null);
		}
	}

	public Dialog onCreateDialog(Bundle savedInstanceState){
		final LinearLayout dialogLayout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.layout_tag_select, null);
		Cursor cursor = new TagListLoader(getActivity()).loadInBackground();
		LinearLayout tagLayout = (LinearLayout) dialogLayout.findViewById(R.id.scrollView).findViewById(R.id.tagLayout);

		for(int i=0;i<cursor.getCount();i++){
			cursor.moveToPosition(i);
			RelativeLayout tagView = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.layout_tag_item, null);
			final EditText editText = (EditText) tagView.findViewById(R.id.tagTextField);
			editText.setText(cursor.getString(1));
			((Button) tagView.findViewById(R.id.removeButton)).setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub

					EditText editText = (EditText) ((RelativeLayout) view.getParent()).findViewById(R.id.tagTextField);

					SQLiteDatabase db = new WordServantDbHelper(getActivity(), WordServantContract.DATABASE_NAME, null, WordServantDbHelper.DATABASE_VERSION).getWritableDatabase();
					String [] columns = {
							WordServantContract.TagEntry._ID,
							WordServantContract.TagEntry.COLUMN_NAME_TAG_NAME
					};
					Cursor cursor = db.query(
							false, WordServantContract.TagEntry.TABLE_NAME,
							columns, WordServantContract.TagEntry.COLUMN_NAME_TAG_NAME+"='"+editText.getText().toString()+"'",
							null, null, null, null, null);
					cursor.moveToFirst();
					db.delete(
							WordServantContract.CategoryEntry.TABLE_NAME, 
							WordServantContract.CategoryEntry.COLUMN_NAME_TAG_ID+"="+cursor.getInt(0), 
							null);
					db.delete(
							WordServantContract.TagEntry.TABLE_NAME, 
							WordServantContract.TagEntry._ID+"="+cursor.getInt(0), 
							null);
					LinearLayout tagList = (LinearLayout) ((ScrollView) dialogLayout.getChildAt(0)).getChildAt(0);
					tagList.removeView((RelativeLayout) view.getParent());
				}
			});
			tagLayout.addView(tagView);

		}

		final AlertDialog alertDialog = new AlertDialog(getActivity()){
			public void onCreate(Bundle savedInstanceState){
				super.onCreate(savedInstanceState);
				this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
			}
		};
		alertDialog.setTitle("Set Tags");
		alertDialog.setView(dialogLayout);
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Done", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}

		});
		alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", (DialogInterface.OnClickListener) null);

		Button addTagButton = (Button) dialogLayout.findViewById(R.id.addNewTag);
		addTagButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				final LinearLayout dialogLayout = (LinearLayout) view.getParent();
				LinearLayout tagLayout = (LinearLayout) dialogLayout.findViewById(R.id.tagLayout);

				final RelativeLayout tagView = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.layout_tag_item, null);
				final EditText editText = (EditText) tagView.findViewById(R.id.tagTextField);

				Button removeTagButton = (Button) tagView.findViewById(R.id.removeButton);
				removeTagButton.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub

						EditText editText = (EditText) ((RelativeLayout) view.getParent()).findViewById(R.id.tagTextField);

						SQLiteDatabase db = new WordServantDbHelper(getActivity(), WordServantContract.DATABASE_NAME, null, WordServantDbHelper.DATABASE_VERSION).getWritableDatabase();
						String [] columns = {
								WordServantContract.TagEntry._ID,
								WordServantContract.TagEntry.COLUMN_NAME_TAG_NAME
						};
						Cursor cursor = db.query(
								false, WordServantContract.TagEntry.TABLE_NAME,
								columns, WordServantContract.TagEntry.COLUMN_NAME_TAG_NAME+"='"+editText.getText().toString()+"'",
								null, null, null, null, null);
						cursor.moveToFirst();
						db.delete(
								WordServantContract.CategoryEntry.TABLE_NAME, 
								WordServantContract.CategoryEntry.COLUMN_NAME_TAG_ID+"="+cursor.getInt(0), 
								null);
						db.delete(
								WordServantContract.TagEntry.TABLE_NAME, 
								WordServantContract.TagEntry._ID+"="+cursor.getInt(0), 
								null);
						LinearLayout tagList = (LinearLayout) ((ScrollView) dialogLayout.getChildAt(0)).getChildAt(0);
						tagList.removeView((RelativeLayout) view.getParent());
					}
				});

				SQLiteDatabase db = new WordServantDbHelper(getActivity(), WordServantContract.DATABASE_NAME, null, WordServantDbHelper.DATABASE_VERSION).getWritableDatabase();
				String [] columns = {
						WordServantContract.TagEntry._ID,
						WordServantContract.TagEntry.COLUMN_NAME_TAG_NAME
				};
				ContentValues cValues = new ContentValues();
				cValues.put(
						WordServantContract.TagEntry.COLUMN_NAME_TAG_NAME, "");
				db.insert(WordServantContract.TagEntry.TABLE_NAME,null, cValues);

				tagLayout.addView(tagView);
				tagView.clearFocus();


			}

		});
		return alertDialog;
	}
}
