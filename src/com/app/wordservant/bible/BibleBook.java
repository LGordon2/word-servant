package com.app.wordservant.bible;

import java.util.ArrayList;

public class BibleBook {

	String mTitle;

	/**
	 * 
	 * @element-type BibleChapter
	 */
	public ArrayList<BibleChapter>  mBibleChapters;

	int mBookNumber;

	public BibleBook(String title){
		mTitle = title;
		mBibleChapters = new ArrayList<BibleChapter>();
	}

	public void addChapter(BibleChapter c) {
		// TODO Auto-generated method stub
		mBibleChapters.add(c);
	}

	public String[] getChaptersArray() {
		// TODO Auto-generated method stub
		String [] allChapters = new String[mBibleChapters.size()];
		for(int i=0;i<mBibleChapters.size();i++){
			allChapters[i]=String.valueOf(mBibleChapters.get(i).mChapterNumber);
		}
		return allChapters;
	}

	public void setBookNumber(int number) {
		// TODO Auto-generated method stub
		this.mBookNumber = number;
	}

	public String toString(){
		return mTitle;
	}
}