package com.app.wordservant;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.CursorLoader;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class AddTagDialogFragment extends DialogFragment {
	private class TagListLoader extends CursorLoader{
		public TagListLoader(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		public Cursor loadInBackground(){
			SQLiteDatabase db = new WordServantDbHelper(getActivity(), WordServantContract.DB_NAME, null, WordServantDbHelper.DATABASE_VERSION).getWritableDatabase();
			String [] columns = {
					WordServantContract.TagEntry._ID,
					WordServantContract.TagEntry.COLUMN_NAME_TAG_NAME
			};
			return db.query(WordServantContract.TagEntry.TABLE_NAME, columns, null, null, null, null, null);
		}
	}

	public Dialog onCreateDialog(Bundle savedInstanceState){
		final RelativeLayout dialogLayout = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.layout_tag_select, null);
		Cursor cursor = new TagListLoader(getActivity()).loadInBackground();
		LinearLayout tagLayout = (LinearLayout) dialogLayout.findViewById(R.id.scrollView).findViewById(R.id.tagLayout);

		RelativeLayout tagView = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.layout_tag_item, null);
		tagLayout.addView(tagView);
		if(cursor.getCount()>0){
			cursor.moveToFirst();
			((EditText) tagView.findViewById(R.id.tagTextField)).setText(cursor.getString(1));
		}
		for(int i=1;i<cursor.getCount();i++){
			cursor.moveToPosition(i);
			tagView = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.layout_tag_item, null);
			((EditText) tagView.findViewById(R.id.tagTextField)).setText(cursor.getString(1));
			tagLayout.addView(tagView);

		}

		Button addTagButton = (Button) dialogLayout.findViewById(R.id.addNewTag);
		final Dialog alertDialog = new AlertDialog.Builder(getActivity())
		.setView(dialogLayout)
		.setTitle("Set tags")
		.setPositiveButton("Done", null)
		.setNegativeButton("Cancel", null)
		.create();


		addTagButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				//alertDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
				//         WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
				final RelativeLayout dialogLayout = (RelativeLayout) view.getParent();
				LinearLayout tagLayout = (LinearLayout) dialogLayout.findViewById(R.id.tagLayout);
				final RelativeLayout tagView = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.layout_tag_item, null);
				EditText editText = (EditText) tagView.findViewById(R.id.tagTextField);
				Button removeTagButton = (Button) tagView.findViewById(R.id.removeButton);
				removeTagButton.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub
						LinearLayout tagList = (LinearLayout) ((ScrollView) dialogLayout.getChildAt(0)).getChildAt(0);
						RelativeLayout tagItemLayout = (RelativeLayout) view.getParent();
						tagList.removeView(tagItemLayout);
					}
				});
				//TextView c = (TextView) tagView.findViewById(R.id.checkBox1);
				//c.performClick();
				tagLayout.addView(tagView);
				InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				//inputManager.restartInput(tagView);
				Toast.makeText(getActivity(), String.valueOf(inputManager.isActive()), Toast.LENGTH_SHORT).show();
				//inputManager.showSoftInput(tagView, InputMethodManager.SHOW_IMPLICIT);
			}

		});
		/*Button removeTagButton = (Button) dialogLayout.findViewById(R.id.removeButton);
		removeTagButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				LinearLayout tagList = (LinearLayout) dialogLayout.findViewById(R.id.scrollView).findViewById(R.id.tagLayout);
				RelativeLayout tagItemLayout = (RelativeLayout) view.getParent();
				tagList.removeView(tagItemLayout);
			}
		});*/
		//ArrayAdapter<String> testAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, projection);
		//cursor.moveToFirst();
		return alertDialog;
	}
}
