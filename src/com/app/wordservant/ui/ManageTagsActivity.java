package com.app.wordservant.ui;
//package com.app.wordservant;
//
//import android.app.Activity;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.os.Bundle;
//import android.support.v4.content.CursorLoader;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//
//public class ManageTagsActivity extends Activity {
//	private class TagListLoader extends CursorLoader{
//		public TagListLoader(Context context) {
//			super(context);
//			// TODO Auto-generated constructor stub
//		}
//
//		public Cursor loadInBackground(){
//			SQLiteDatabase db = new WordServantDbHelper(ManageTagsActivity.this, WordServantContract.DATABASE_NAME, null, WordServantDbHelper.DATABASE_VERSION).getWritableDatabase();
//			String [] columns = {
//					WordServantContract.TagEntry._ID,
//					WordServantContract.TagEntry.COLUMN_NAME_TAG_NAME
//			};
//			return db.query(WordServantContract.TagEntry.TABLE_NAME, columns, null, null, null, null, null);
//		}
//	}
//	
//	public void onCreate(Bundle savedInstanceState){
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_manage_tags);
//	}
//	public void onStart(){
//		super.onStart();
//		
//		//Load all the tags.
//		Cursor cursor = new TagListLoader(this).loadInBackground();
//		final LinearLayout tagLayout = (LinearLayout) findViewById(R.id.tagLayout);
//		
//		for(int i=0;i<cursor.getCount();i++){
//			cursor.moveToPosition(i);
//			LinearLayout tagItem = (LinearLayout) getLayoutInflater().inflate(R.layout.manage_tags_item, null);
//			((EditText) tagItem.findViewById(R.id.tagText)).setText(cursor.getString(1));
//			tagLayout.addView(tagItem);
//		}
//		Button button = (Button) findViewById(R.id.addTagButton);
//		button.setOnClickListener(new OnClickListener(){
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				LinearLayout tagItem = (LinearLayout) getLayoutInflater().inflate(R.layout.manage_tags_item, null);
//				tagLayout.addView(tagItem);
//			}
//			
//		});
//	}
//}
