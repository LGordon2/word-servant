package com.app.wordservant.bible;

public class BibleVerse {

	public Integer mVerseNumber;

	public String mVerseText;

	public BibleVerse(Integer verseNumber, String verseText){
		mVerseNumber = verseNumber;
		mVerseText = verseText;
	}

	public BibleVerse(Integer verseNumber) {
		// TODO Auto-generated constructor stub
		mVerseNumber = verseNumber;
	}

	public void setText(String text) {
		// TODO Auto-generated method stub
		mVerseText = text;
	}
}