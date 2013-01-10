package com.app.wordservant.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.app.wordservant.R;

public class SelectScriptureFragmentActivity extends SherlockFragmentActivity{
    private FragmentTabHost mTabHost;
    protected int bookNumber = 0;
    protected String bookName = "";
	protected int chapterNumber = 0;

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
    }
    public void setCurrentTab(int tabNumber){
    	mTabHost.setCurrentTab(tabNumber);
    }
}
