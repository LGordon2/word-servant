package com.app.wordservant.util;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

import com.app.wordservant.ui.Bible;

public class BibleImporter implements Runnable{

	InputStream mInputStream;
	
	public BibleImporter(InputStream inputStream){
		mInputStream = inputStream;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//Bible bible = Bible.getInstance();
		Bible.getInstance().setImportStatus(Constants.IMPORT_STATUS_IMPORTING);
		Bible.BibleBook b = null;
		Bible.BibleChapter c = null;
		Bible.BibleVerse v = null;
		Integer currentBook = -1;
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(mInputStream, null);

			int eventType = xpp.getEventType();
			Integer chapterNumber=-1;
			Integer verseNumber=-1;
			while(eventType != XmlPullParser.END_DOCUMENT){

				if(eventType == XmlPullParser.START_TAG){
					if(xpp.getName().equals("bible")){
						currentBook += 1;
					}else if(xpp.getName().equals("bookname")){		
						//Get the book and check if it exists.
						try{
							Bible.getInstance().getBook(currentBook);
						}catch(NullPointerException e){
							b = Bible.getInstance().new BibleBook(currentBook,xpp.nextText());
							Bible.getInstance().addBook(b);
						}
					}else if(xpp.getName().equals("chapter")){
						//Check for the chapter.
						String chapterNumberText = xpp.nextText();
						try{
							chapterNumber = Integer.decode(chapterNumberText);
						}catch(NumberFormatException e){
							Log.e("wordservanterror", "Chapter number was invalid.");
							throw(e);
						}
						
						//Get the book and check if it exists.
						try{
							Bible.getInstance().getBook(currentBook).getChapter(chapterNumber-1);
						}catch(IndexOutOfBoundsException e){
							c = Bible.getInstance().new BibleChapter(chapterNumber);
							Bible.getInstance().getBook(currentBook).addChapter(c);
						}
					}else if(xpp.getName().equals("verse")){
						//Check for the verse.
						String verseNumberText = xpp.nextText();
						try{
							verseNumber = Integer.valueOf(verseNumberText);
						}catch(NumberFormatException e){
							Log.e("wordservanterror", "Verse number was invalid.");
							throw(e);
						}
						
						//Get the book and check if it exists.
						try{
							Bible.getInstance().getBook(currentBook).getChapter(chapterNumber-1).getVerse(verseNumber-1);
						}catch(IndexOutOfBoundsException e){
							v = Bible.getInstance().new BibleVerse(verseNumber);
							Bible.getInstance().getBook(currentBook).getChapter(chapterNumber-1).addVerse(v);
						}
					}else if(xpp.getName().equals("text")){
						Bible.getInstance().getBook(currentBook).getChapter(chapterNumber-1).getVerse(verseNumber-1).setText(xpp.nextText());
					}

				}
				eventType = xpp.next();
			}
			//KJV
//			while(eventType != XmlPullParser.END_DOCUMENT){
//				if(eventType == XmlPullParser.START_TAG){
//					if(xpp.getName().equals("BIBLEBOOK")){
//						currentBook += 1;
//						b = Bible.getInstance().new BibleBook(currentBook, xpp.getAttributeValue(null, "bname"));
//					}else if(xpp.getName().equals("CHAPTER")){
//						c = Bible.getInstance().new BibleChapter(xpp.getAttributeValue(null, "cnumber"));
//					}else if(xpp.getName().equals("VERS")){
//						v = Bible.getInstance().new BibleVerse(xpp.getAttributeValue(null, "vnumber"),xpp.nextText());
//						c.addVerse(v);
//					}
//
//				}else if(eventType == XmlPullParser.END_TAG){
//					if(xpp.getName().equals("CHAPTER") && currentBook != null){
//						b.addChapter(c);
//					}else if(xpp.getName().equals("BIBLEBOOK")){
//						Bible.getInstance().addBook(b);
//					}
//				}
//				eventType = xpp.next();
//			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Bible.getInstance().setImportStatus(Constants.IMPORT_STATUS_COMPLETED);
	}

}
