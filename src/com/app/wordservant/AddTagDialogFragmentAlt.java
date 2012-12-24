//package com.app.wordservant;
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.DialogInterface.OnClickListener;
//import android.content.Intent;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.os.Bundle;
//import android.support.v4.app.DialogFragment;
//import android.support.v4.content.CursorLoader;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.SimpleCursorAdapter;
//
//public class AddTagDialogFragmentAlt extends DialogFragment {
//	private class TagListLoader extends CursorLoader{
//		public TagListLoader(Context context) {
//			super(context);
//			// TODO Auto-generated constructor stub
//		}
//
//		public Cursor loadInBackground(){
//			SQLiteDatabase db = new WordServantDbHelper(getActivity(), WordServantContract.DATABASE_NAME, null, WordServantDbHelper.DATABASE_VERSION).getWritableDatabase();
//			String [] columns = {
//					WordServantContract.TagEntry._ID,
//					WordServantContract.TagEntry.COLUMN_NAME_TAG_NAME
//			};
//			return db.query(WordServantContract.TagEntry.TABLE_NAME, columns, null, null, null, null, null);
//		}
//	}
//	
//	public Dialog onCreateDialog(Bundle savedInstanceState){
//		Cursor cursor = new TagListLoader(getActivity()).loadInBackground();
//		ListView listView = (ListView) new ListView(getActivity());
//		Button button = new Button(getActivity());
//		button.setText("Manage Tags");
//		button.setOnClickListener(new View.OnClickListener(){
//
//			@Override
//			public void onClick(View view) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(getActivity(),ManageTagsActivity.class);
//				startActivity(intent);
//			}
//			
//		});
//		listView.addFooterView(button);
//		SimpleCursorAdapter adapter = new SimpleCursorAdapter(
//				getActivity(), 
//				R.layout.layout_tag_select_alternate, 
//				cursor, 
//				new String []{WordServantContract.TagEntry.COLUMN_NAME_TAG_NAME}, 
//				new int []{R.id.checkBox1});
//		listView.setAdapter(adapter);
//		AlertDialog alarmDialog = new AlertDialog.Builder(getActivity())
//		.setTitle("Set tags")
//		.setView(listView)
//		.setPositiveButton("Done", new OnClickListener(){
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//		})
//		.setNegativeButton("Cancel", new OnClickListener(){
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//		})
//		.create();
//		return alarmDialog;
//	}
//}
