package com.app.wordservant.ui;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.TreeMap;


public class Bible{
	private static class BibleHolder { 
		public static final Bible INSTANCE = new Bible();
	}
	public static Bible getInstance() {
		return BibleHolder.INSTANCE;
	}
	TreeMap<Integer, String> bookNumbers;
	Hashtable<String, BibleBook> books;
	private Bible(){
		books = new Hashtable<String,BibleBook>();
		bookNumbers = new TreeMap<Integer, String>();
	}
	public String[] getBookNames() {
		// TODO Auto-generated method stub
		return bookNumbers.values().toArray(new String[]{});
	}
	public void addBook(BibleBook b) {
		// TODO Auto-generated method stub
		bookNumbers.put(b.bookNumber, b.bookName);
		books.put(b.bookName, b);
	}
	public BibleBook getBook(int position) {
		// TODO Auto-generated method stub
		return this.getBook(bookNumbers.get(position));
	}
	public BibleBook getBook(String name){
		return books.get(name);
	}


	public class BibleBook{
		String bookName;
		int bookNumber;
		ArrayList<BibleChapter> chapters;
		public BibleBook(int bookNumber, String bookName) {
			// TODO Auto-generated constructor stub
			this.bookNumber = bookNumber;
			this.bookName = bookName;
			chapters = new ArrayList<BibleChapter>();
		}
		public void addChapter(BibleChapter c){
			chapters.add(c);
		}
		public String[] getChaptersArray() {
			// TODO Auto-generated method stub
			ArrayList<String> allChapters = new ArrayList<String>();
			for(BibleChapter c : chapters){
				allChapters.add(c.chapterNumber);
			}
			return allChapters.toArray(new String[]{});
		}
		public BibleChapter getChapter(int position) {
			// TODO Auto-generated method stub
			return chapters.get(position);
		}

	}
	public class BibleChapter{
		String chapterNumber;
		ArrayList<BibleVerse> verses;
		public BibleChapter(String chapterNumber){
			this.chapterNumber = chapterNumber;
			verses = new ArrayList<BibleVerse>();
		}
		public void addVerse(BibleVerse v){
			verses.add(v);
		}
		public String[] getVersesArray() {
			// TODO Auto-generated method stub
			ArrayList<String> s = new ArrayList<String>();
			for(BibleVerse v : verses){
				s.add(v.verseNumber);
			}
			return s.toArray(new String[]{});
		}
		public String getVerseText(int position) {
			// TODO Auto-generated method stub
			return verses.get(position).text;
		}
		public BibleVerse getVerse(int position) {
			// TODO Auto-generated method stub
			return verses.get(position);
		}
	}
	public class BibleVerse{
		String verseNumber;
		String text;
		public BibleVerse(String verseNumber, String text){
			this.verseNumber = verseNumber;
			this.text = text;
		}
	}
}
