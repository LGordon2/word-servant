package com.app.wordservant.util;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

import com.app.wordservant.bible.Bible;
import com.app.wordservant.bible.BibleBook;
import com.app.wordservant.bible.BibleChapter;
import com.app.wordservant.bible.BibleVerse;

public class BibleImporter implements Runnable{

	InputStream mInputStream;
	public BibleImporter(InputStream inputStream){
		mInputStream = inputStream;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Bible bible = Bible.getInstance();
		Bible.getInstance().setImporting();

		BibleBook b = null;
		BibleChapter c = null;
		BibleVerse v = null;
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(mInputStream, null);

			int eventType = xpp.getEventType();
			while((eventType = xpp.next()) != XmlPullParser.END_DOCUMENT){
				if(eventType == XmlPullParser.START_TAG){
					if(xpp.getName().equals("bookname")){		
						//Get the book and check if it exists.
						String bookTitle = xpp.nextText();
						if((b = bible.getBook(bookTitle))==null){
							b = new BibleBook(bookTitle);
							bible.addBook(b);
						}
					}else if(xpp.getName().equals("chapter")){
						Integer chapterNumber;
						//Check for the chapter.
						try{
							chapterNumber = Integer.decode(xpp.nextText());
						}catch(NumberFormatException e){
							Log.e("wordservanterror", "Chapter number was invalid.");
							throw(e);
						}

						//Get the book and check if it exists.
						try{
							c = b.mBibleChapters.get(chapterNumber-1);
						}catch(IndexOutOfBoundsException e){
							assert b!=null;
							c = new BibleChapter(chapterNumber);
							b.addChapter(c);
						}
					}else if(xpp.getName().equals("verse")){
						Integer verseNumber;
						//Check for the verse.
						try{
							verseNumber = Integer.valueOf(xpp.nextText());
						}catch(NumberFormatException e){
							Log.e("wordservanterror", "Verse number was invalid.");
							throw(e);
						}

						//Get the book and check if it exists.
						try{
							v = c.mBibleVerses.get(verseNumber-1);
						}catch(IndexOutOfBoundsException e){
							assert c!=null;
							v = new BibleVerse(verseNumber);
							c.addVerse(v);
						}
					}else if(xpp.getName().equals("text")){
						v.setText(xpp.nextText());
					}
				}
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
		Bible.getInstance().importDone();
	}
}
