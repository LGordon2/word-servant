package com.app.wordservant.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.app.wordservant.R;

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
        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(),R.id.realtabcontent);
        mTabHost.addTab(mTabHost.newTabSpec("books").setIndicator("Books"),
        		SelectBibleBookFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("chapters").setIndicator("Chapters"),
        		SelectBibleChapterFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("verses").setIndicator("Verses"),
        		SelectBibleVerseFragment.class, null);
        
        if(savedInstanceState!=null){
            bookNumber = savedInstanceState.getInt("bookNumber");
            bookName = savedInstanceState.getString("bookName");
        	chapterNumber = savedInstanceState.getInt("chapterNumber");
        	mTabHost.setCurrentTab(savedInstanceState.getInt("tabNumber"));
        	mVerseNumbers = savedInstanceState.getIntegerArrayList("verseNumbers");
        	checkedActivatedFix = savedInstanceState.getBoolean("checkedActivatedFix");
        }
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
}
