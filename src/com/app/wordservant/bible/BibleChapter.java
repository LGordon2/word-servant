package com.app.wordservant.bible;

import java.util.ArrayList;

public class BibleChapter {

	Integer mChapterNumber;

	/**
	 * 
	 * @element-type BibleVerse
	 */
	public ArrayList<BibleVerse> mBibleVerses;

	public BibleChapter(){
		mBibleVerses = new ArrayList<BibleVerse>();
		mChapterNumber = 0;
	}

	public BibleChapter(Integer chapterNumber) {
		// TODO Auto-generated constructor stub
		mBibleVerses = new ArrayList<BibleVerse>();
		mChapterNumber = chapterNumber;
	}

	public void addVerse(BibleVerse v) {
		// TODO Auto-generated method stub
		mBibleVerses.add(v);
	}

	public String [] getVersesArray() {
		// TODO Auto-generated method stub
		String [] allVerses = new String[mBibleVerses.size()];
		for(int i=0;i<mBibleVerses.size();i++){
			allVerses[i]=String.valueOf(mBibleVerses.get(i).mVerseNumber);
		}
		return allVerses;
	}

}