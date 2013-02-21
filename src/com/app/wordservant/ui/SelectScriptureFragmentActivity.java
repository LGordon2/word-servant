package com.app.wordservant.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.NavUtils;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.app.wordservant.R;
import com.app.wordservant.bible.Bible;

public class SelectScriptureFragmentActivity extends SherlockFragmentActivity{
	private FragmentTabHost mTabHost;
	protected int bookNumber = 0;
	protected String bookName = "Genesis";
	protected int chapterNumber = 0;
	protected boolean checkedActivatedFix = true;
	protected ArrayList<Integer> mVerseNumbers = new ArrayList<Integer>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_scripture);
		// Show the Up button in the action bar.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(),R.id.realtabcontent);
		mTabHost.addTab(mTabHost.newTabSpec("books").setIndicator("Books"),
				SelectBibleBookFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("chapters").setIndicator("Chapters"),
				SelectBibleChapterFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("verses").setIndicator("Verses"),
				SelectBibleVerseFragment.class, null);

		if(Build.VERSION.SDK_INT>=11){
			for(int i=0;i<mTabHost.getTabWidget().getChildCount();i++) 
			{
				LinearLayout layout = (LinearLayout) mTabHost.getTabWidget().getChildAt(i);
				layout.setBackgroundResource(R.drawable.tab_indicator_holo);
			} 
		}
		if(savedInstanceState!=null){
			bookNumber = savedInstanceState.getInt("bookNumber");
			bookName = savedInstanceState.getString("bookName");
			chapterNumber = savedInstanceState.getInt("chapterNumber");
			mTabHost.setCurrentTab(savedInstanceState.getInt("tabNumber"));
			mVerseNumbers = savedInstanceState.getIntegerArrayList("verseNumbers");
			checkedActivatedFix = savedInstanceState.getBoolean("checkedActivatedFix");
		}
	}
	public void onStart(){
		super.onStart();
		mTabHost.getTabWidget().setEnabled(Bible.getInstance().isImported());
	}
	public void setCurrentTab(int tabNumber){
		mTabHost.setCurrentTab(tabNumber);
	}
	protected void onActivityResult (int requestCode, int resultCode, Intent data){
		if(resultCode==0){
			finish();
		}
	}
	protected void onSaveInstanceState(Bundle outState){
		outState.putInt("bookNumber", bookNumber);
		outState.putString("bookName", bookName);
		outState.putInt("chapterNumber", chapterNumber);
		outState.putInt("tabNumber", mTabHost.getCurrentTab());
		outState.putIntegerArrayList("verseNumbers", mVerseNumbers);
		outState.putBoolean("checkedActivatedFix", checkedActivatedFix);
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
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public void updateBook(Integer bookNumber, String bookName){
		this.bookName = bookName;
		this.bookNumber = bookNumber;
		chapterNumber = 0;
		mVerseNumbers.clear();
		
		this.setCurrentTab(1);
	}
	
	public void updateChapter(Integer chapterNumber){
		this.chapterNumber = chapterNumber;
		mVerseNumbers.clear();
		
		this.setCurrentTab(2);
	}
	public void enableTabWidget(){
		this.mTabHost.getTabWidget().setEnabled(true);
	}
}
