package com.app.wordservant.bible;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

import com.app.wordservant.util.Constants;

public class Bible {

	String mName;

	public String mImportStatus;
	/**
	 * 
	 * @element-type BibleBook
	 */
	public TreeSet<BibleBook> mBooks;

	public Boolean isImported() {
		return mImportStatus.equals(Constants.IMPORT_STATUS_COMPLETED)?true:false;
	}

	private static class BibleHolder { 
		public static final Bible INSTANCE = new Bible();
	}
	public static Bible getInstance() {
		return BibleHolder.INSTANCE;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Bible(){
		mBooks = new TreeSet<BibleBook>(new Comparator(){

			@Override
			public int compare(Object lhs, Object rhs) {
				// TODO Auto-generated method stub
				int lhsBookNumber = ((BibleBook) lhs).mBookNumber;
				int rhsBookNumber = ((BibleBook) rhs).mBookNumber;
				if(lhsBookNumber < rhsBookNumber)
					return -1;
				else if(lhsBookNumber == rhsBookNumber)
					return 0;
				return 1;
			}
			
		});
		mImportStatus = Constants.IMPORT_STATUS_NOT_STARTED;
	}

	public void setImporting() {
		// TODO Auto-generated method stub
		mImportStatus = Constants.IMPORT_STATUS_IMPORTING;
	}

	public void importDone() {
		// TODO Auto-generated method stub
		mImportStatus = Constants.IMPORT_STATUS_COMPLETED;
	}

	public void addBook(BibleBook b) {
		// TODO Auto-generated method stub
		b.setBookNumber(mBooks.size());
		mBooks.add(b);
	}

	public BibleBook getBook(String bookName) {
		// TODO Auto-generated method stub
		Iterator<BibleBook> itr = mBooks.iterator();
		BibleBook book;
		while(itr.hasNext()){
			if((book = itr.next()).mTitle.equals(bookName)){
				return book;
			}
		}
		return null;
	}
	
	public BibleBook getBook(int bookNumber) {
		// TODO Auto-generated method stub
		Iterator<BibleBook> itr = mBooks.iterator();
		BibleBook book;
		while(itr.hasNext()){
			if((book = itr.next()).mBookNumber == bookNumber){
				return book;
			}
		}
		return null;
	}

	public String[] getBookNames() {
		// TODO Auto-generated method stub
		String [] allBookNames = new String[mBooks.size()];
		Iterator<BibleBook> itr = mBooks.iterator();
		int i=0;
		while(itr.hasNext()){
			allBookNames[i]=itr.next().mTitle;
			i+=1;
		}
		return allBookNames;
	}

}